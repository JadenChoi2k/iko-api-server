package com.iko.restapi.service.order;

import com.iko.restapi.common.exception.EntityNotFoundException;
import com.iko.restapi.common.exception.InvalidAccessException;
import com.iko.restapi.common.utils.SecurityUtils;
import com.iko.restapi.domain.order.Order;
import com.iko.restapi.domain.order.OrderCancelItem;
import com.iko.restapi.domain.order.OrderItem;
import com.iko.restapi.domain.user.User;
import com.iko.restapi.repository.order.OrderItemJpaRepository;
import com.iko.restapi.repository.order.OrderJpaRepository;
import com.iko.restapi.repository.user.UserJpaRepository;
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
    private final UserJpaRepository userRepository;
    private final OrderJpaRepository orderRepository;
    private final OrderItemJpaRepository orderItemRepository;

    // 카트에 있는 장바구니 아이템을 기반으로 주문 생성
    public Order createOrder(List<Long> cartItemIdList) {
        User user = currentUser();
        log.info("create order (userId={}, itemIds={})", user.getId(), cartItemIdList.stream().map(Object::toString).collect(Collectors.joining(",")));
        return orderRepository.createOrderByUserAndCartIdList(user, cartItemIdList);
    }

    public Order fillDeliveryInfo(Long orderId, String recipient, String address, String zipCode) {
        Order order = fetchOrderAuthorized(orderId);
        log.info("order(id={}) fill delivery info", orderId);
        order.fillDeliveryInfo(recipient, address, zipCode);
        return order;
    }

    // TODO: 결제 시스템 연동
    public Order pay(Long orderId) {
        Order order = fetchOrderAuthorized(orderId);
        log.info("pay order(id={})", orderId);
        order.getOrderItems()
                .forEach(OrderItem::pay);
        return order;
    }

    // todo: 결제 시스템 연동하여 결제 취소 -> 완료
    public Order cancelPayment(Long orderId) {
        Order order = fetchOrderAuthorized(orderId);
        log.info("cancel payment (orderId={})", orderId);
        order.getOrderItems()
                .forEach(OrderItem::cancelPayment);
        return order;
    }

    // 관리자, 판매자만 접근
    public Order readyOrderProduct(Long orderId) {
        validateSellerOrAdmin();
        Order order = fetchOrder(orderId);
        log.info("ready product (orderId={})", orderId);
        order.getOrderItems()
                .forEach(OrderItem::readyProduct);
        return order;
    }

    // 관리자, 판매자만 접근
    public Order readyOrderDelivery(Long orderId) {
        validateSellerOrAdmin();
        Order order = fetchOrder(orderId);
        log.info("ready delivery (orderId={})", orderId);
        order.getOrderItems()
                .forEach(OrderItem::readyDelivery);
        return order;
    }

    // 배송 관련은 관리자, 판매자만 접근
//    public List<OrderItem> registerDeliveryOne(List<Long> orderItemIds, String deliveryCode, String deliveryProvider) {
//        validateSellerOrAdmin();
//        log.info("register delivery");
//        return orderItemIds.stream()
//                .map(this::fetchOrderItem)
//                .peek((item) -> item.registerDelivery(deliveryCode, deliveryProvider))
//                .collect(Collectors.toList());
//    }

    public Order registerDeliveryAll(Long orderId, String deliveryCode, String deliveryProvider) {
        validateSellerOrAdmin();
        Order order = fetchOrder(orderId);
        log.info("register delivery (orderId={})", orderId);
        order.getOrderItems()
                .forEach((item) -> item.registerDelivery(deliveryCode, deliveryProvider));
        return order;
    }

    // 자동으로 등록될 수 있도록 하면 좋을 것 같습니다
//    private OrderItem deliveryDoneOne(Long orderItemId) {
//        validateSellerOrAdmin();
//        OrderItem orderItem = fetchOrderItem(orderItemId);
//        orderItem.doneDelivery();
//        return orderItem;
//    }
//
//    public List<OrderItem> deliveryDone(List<Long> itemIds) {
//        validateSellerOrAdmin();
//        return itemIds.stream()
//                .map(this::deliveryDoneOne)
//                .collect(Collectors.toList());
//    }

    public Order deliveryDone(Long orderId) {
        Order order = fetchOrder(orderId);
        log.info("delivery done (orderId={})", orderId);
        order.getOrderItems().forEach(OrderItem::doneDelivery);
        return order;
    }

    public List<OrderItem> completeOrderItems(List<Long> orderItemIdList) {
        log.info("order items complete (orderItemIdList=[{}])",
                orderItemIdList.stream().map(Object::toString).collect(Collectors.joining(",")));
        return orderItemIdList.stream()
                .map(this::fetchOrderItemAuthorized)
                .peek(OrderItem::completeOrder)
                .collect(Collectors.toList());
    }

    public Order completeOrder(Long orderId) {
        Order order = fetchOrderAuthorized(orderId);
        log.info("complete order(orderId={})", orderId);
        order.getOrderItems().forEach(OrderItem::completeOrder);
        return order;
    }

    public List<OrderItem> refundOrderItems(List<Long> orderItemIds) {
        log.info("order items refund (orderItemIdList=[{}])",
                orderItemIds.stream().map(Object::toString).collect(Collectors.joining(",")));
        return orderItemIds.stream()
                .map(this::fetchOrderItemAuthorized)
                .peek(OrderItem::refund)
                .collect(Collectors.toList());
    }

    public List<OrderItem> refundOrderItems(Long orderId, List<Long> orderItemIdList) {
        log.info("order items refund (orderId={})(orderItemIdList=[{}])",
                orderId, orderItemIdList.stream().map(Object::toString).collect(Collectors.joining(",")));
        Order order = fetchOrderAuthorized(orderId);
        return order.getOrderItems().stream()
                .filter((item) -> orderItemIdList.contains(item.getId()))
                .peek(OrderItem::refund)
                .collect(Collectors.toList());
    }

    public List<OrderItem> exchangeOrderItems(List<Long> orderItemIdList) {
        log.info("order items exchange (orderItemIdList=[{}])",
                orderItemIdList.stream().map(Object::toString).collect(Collectors.joining(",")));
        return orderItemIdList.stream()
                .map(this::fetchOrderItemAuthorized)
                .peek(OrderItem::exchange)
                .collect(Collectors.toList());
    }

    public List<OrderItem> exchangeOrderItems(Long orderId, List<Long> orderItemIdList) {
        log.info("order items exchange (orderId={})(orderItemIdList=[{}])",
                orderId, orderItemIdList.stream().map(Object::toString).collect(Collectors.joining(",")));
        Order order = fetchOrderAuthorized(orderId);
        return order.getOrderItems().stream()
                .filter((item) -> orderItemIdList.contains(item.getId()))
                .peek(OrderItem::exchange)
                .collect(Collectors.toList());
    }
    
    public List<OrderItem> registerCancelDelivery(Long orderId, List<Long> orderItemIdList, String deliveryCode, String deliveryProvider) {
        log.info("register cancel delivery (orderId={})(orderItemIdList=[{}])",
                orderId, orderItemIdList.stream().map(Object::toString).collect(Collectors.joining(",")));
        Order order = fetchOrderAuthorized(orderId);
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
        log.info("complete all canceled (orderId={})", orderId);
        order.getOrderItems().stream()
                .filter(OrderItem::isCanceled)
                .map(OrderItem::getOrderCancelItem)
                .forEach(OrderCancelItem::complete);
        return order;
    }
    
    private OrderItem completeCancelOne(Long orderItemId) {
        log.info("complete order item canceled (orderItemId={})", orderItemId);
        OrderItem orderItem = fetchOrderItem(orderItemId);
        if (!orderItem.isCanceled()) {
            throw new InvalidAccessException("취소 내역을 찾을 수 없습니다");
        }
        orderItem.getOrderCancelItem().complete();
        return orderItem;
    }

    public List<OrderItem> completeCancel(List<Long> itemIds) {
        return itemIds.stream()
                .map(this::completeCancelOne)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Order> findAllOrder(int page, int size) {
        return orderRepository.findAll(PageRequest.of(page, size)).toList();
    }

    @Transactional(readOnly = true)
    public Order findOneOrder(Long orderId) {
        return fetchOrder(orderId);
    }

    /* private methods */

    private User currentUser() {
        return SecurityUtils.getCurrentUser(userRepository);
    }

    private Order fetchOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 주문입니다"));
    }

    private Order fetchOrderAuthorized(User user, Long orderId) {
        var order = fetchOrder(orderId);
        if (!order.getUser().equals(user)) {
            throw new InvalidAccessException("권한이 없습니다");
        }
        return order;
    }

    private Order fetchOrderAuthorized(Long orderId) {
        return fetchOrderAuthorized(currentUser(), orderId);
    }

    private OrderItem fetchOrderItem(Long orderItemId) {
        return orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 주문 아이템입니다"));
    }

    private OrderItem fetchOrderItemAuthorized(User user, Long orderItemId) {
        var orderItem = fetchOrderItem(orderItemId);
        if (!orderItem.getOrder().getUser().equals(user)) {
            throw new InvalidAccessException("권한이 없습니다");
        }
        return orderItem;
    }

    private OrderItem fetchOrderItemAuthorized(Long orderItemId) {
        return fetchOrderItemAuthorized(currentUser(), orderItemId);
    }

    private void validateSellerOrAdmin() {
        User.Role role = currentUser().getRole();
        if (!role.equals(User.Role.ROLE_SELLER) && !role.equals(User.Role.ROLE_ADMIN)) {
            throw new InvalidAccessException();
        }
    }
}
