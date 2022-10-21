package com.iko.restapi.service.order;

import com.iko.restapi.common.exception.EntityNotFoundException;
import com.iko.restapi.common.exception.InvalidAccessException;
import com.iko.restapi.domain.order.Order;
import com.iko.restapi.domain.order.OrderCancelItem;
import com.iko.restapi.domain.order.OrderItem;
import com.iko.restapi.repository.order.OrderItemJpaRepository;
import com.iko.restapi.repository.order.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderJpaRepository orderRepository;
    private final OrderItemJpaRepository orderItemRepository;

    // 카트에 있는 장바구니 아이템을 기반으로 주문 생성
    public Order createOrder(Long userId, List<Long> cartItemIdList) {
        return orderRepository.createOrderByUserIdAndCartIdList(userId,cartItemIdList);
    }

    // TODO: 결제 시스템 연동
    public Order pay(Long userId, Long orderId) {
        Order order = fetchOrderAuthorized(userId, orderId);
        order.getOrderItems()
                .forEach(OrderItem::pay);
        return order;
    }

    // todo: 결제 시스템 연동하여 결제 취소 -> 완료
    public Order cancelPayment(Long userId, Long orderId) {
        Order order = fetchOrderAuthorized(userId, orderId);
        order.getOrderItems()
                .forEach(OrderItem::cancelPayment);
        return order;
    }

    // 배송 관련은 관리자, 판매자만 접근
    public OrderItem registerDeliveryOne(Long orderItemId, String deliveryCode, String deliveryProvider) {
        OrderItem orderItem = fetchOrderItem(orderItemId);
        orderItem.registerDelivery(deliveryCode, deliveryProvider);
        return orderItem;
    }

    public Order registerDeliveryAll(Long orderId, String deliveryCode, String deliveryProvider) {
        Order order = fetchOrder(orderId);
        order.getOrderItems()
                .forEach((item) -> item.registerDelivery(deliveryCode, deliveryProvider));
        return order;
    }

    // 자동으로 등록될 수 있도록 하면 좋을 것 같습니다
    public OrderItem deliveryDone(Long orderItemId) {
        OrderItem orderItem = fetchOrderItem(orderItemId);
        orderItem.doneDelivery();
        return orderItem;
    }

    public OrderItem completeOneOrderItem(Long userId, Long orderItemId) {
        OrderItem orderItem = fetchOrderItemAuthorized(userId, orderItemId);
        orderItem.completeOrder();
        return orderItem;
    }

    public Order completeOrder(Long userId, Long orderId) {
        Order order = fetchOrderAuthorized(userId, orderId);
        order.getOrderItems().forEach(OrderItem::completeOrder);
        return order;
    }

    public OrderItem refundOrderItem(Long userId, Long orderItemId) {
        OrderItem orderItem = fetchOrderItemAuthorized(userId, orderItemId);
        orderItem.refund();
        return orderItem;
    }

    public List<OrderItem> refundOrderItems(Long userId, Long orderId, List<Long> orderItemIdList) {
        Order order = fetchOrderAuthorized(userId, orderId);
        return order.getOrderItems().stream()
                .filter((item) -> orderItemIdList.contains(item.getId()))
                .peek(OrderItem::refund)
                .collect(Collectors.toList());
    }

    public OrderItem exchangeOrderItem(Long userId, Long orderItemId) {
        OrderItem orderItem = fetchOrderItemAuthorized(userId, orderItemId);
        orderItem.exchange();
        return orderItem;
    }

    public List<OrderItem> exchangeOrderItems(Long userId, Long orderId, List<Long> orderItemIdList) {
        Order order = fetchOrderAuthorized(userId, orderId);
        return order.getOrderItems().stream()
                .filter((item) -> orderItemIdList.contains(item.getId()))
                .peek(OrderItem::exchange)
                .collect(Collectors.toList());
    }
    
    public List<OrderItem> registerCancelDelivery(Long userId, Long orderId, List<Long> orderItemIdList, String deliveryCode, String deliveryProvider) {
        Order order = fetchOrderAuthorized(userId, orderId);
        Stream<OrderItem> orderItemStream = order.getOrderItems().stream()
                .filter((item) -> orderItemIdList.contains(item.getId()));
        orderItemStream
                .map(OrderItem::getOrderCancelItem)
                .map(Optional::of)
                .map(e -> e.orElseThrow(() -> new EntityNotFoundException("취소 요청을 찾을 수 없습니다")))
                .forEach(can -> can.registerDelivery(deliveryCode, deliveryProvider));
        return orderItemStream.collect(Collectors.toList());
    }

    // 관리자, 판매자만 접근
    public Order completeAllCancel(Long orderId) {
        Order order = fetchOrder(orderId);
        order.getOrderItems().stream()
                .filter(OrderItem::isCanceled)
                .map(OrderItem::getOrderCancelItem)
                .forEach(OrderCancelItem::complete);
        return order;
    }
    
    public OrderItem completeOneCancel(Long orderItemId) {
        OrderItem orderItem = fetchOrderItem(orderItemId);
        if (!orderItem.isCanceled()) {
            throw new InvalidAccessException("취소 내역을 찾을 수 없습니다");
        }
        orderItem.getOrderCancelItem().complete();
        return orderItem;
    }

    @Transactional(readOnly = true)
    public List<Order> findAllOrder(int page, int size) {
        return orderRepository.findAll(PageRequest.of(page, size)).toList();
    }

    @Transactional(readOnly = true)
    public Order findOneOrder(Long orderId) {
        return fetchOrder(orderId);
    }

    private Order fetchOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 주문입니다"));
    }

    private Order fetchOrderAuthorized(Long userId, Long orderId) {
        var order = fetchOrder(orderId);
        if (!order.getUser().getId().equals(userId)) {
            throw new InvalidAccessException("권한이 없습니다");
        }
        return order;
    }

    private OrderItem fetchOrderItem(Long orderItemId) {
        return orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 주문 아이템입니다"));
    }

    private OrderItem fetchOrderItemAuthorized(Long userId, Long orderItemId) {
        var orderItem = fetchOrderItem(orderItemId);
        if (!orderItem.getOrder().getUser().getId().equals(userId)) {
            throw new InvalidAccessException("권한이 없습니다");
        }
        return orderItem;
    }
}
