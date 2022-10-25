package com.iko.restapi.controller.order;

import com.iko.restapi.common.exception.InvalidAccessException;
import com.iko.restapi.common.response.CommonResponse;
import com.iko.restapi.common.response.PaginationResponse;
import com.iko.restapi.common.utils.SessionUtils;
import com.iko.restapi.domain.order.Order;
import com.iko.restapi.domain.order.OrderItem;
import com.iko.restapi.dto.order.OrderDto;
import com.iko.restapi.dto.order.OrderItemDto;
import com.iko.restapi.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api/v1/order")
@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public CommonResponse<OrderDto.Main> createOrder(HttpServletRequest request, @RequestBody OrderDto.CreateRequest createRequest) {
        Order order = orderService.createOrder(SessionUtils.getUserId(request), createRequest.getCartItemIdList());
        return CommonResponse.success(OrderDto.Main.of(order));
    }

    @GetMapping
    public PaginationResponse<OrderDto.Main> findAllOrder(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        List<Order> result = orderService.findAllOrder(page, size);
        return PaginationResponse.success(
                page,
                "date",
                result.stream()
                        .map(OrderDto.Main::of)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{orderId}")
    public CommonResponse<OrderDto.Main> findOneOrder(HttpServletRequest request, @PathVariable Long orderId) {
        Order order = orderService.findOneOrder(orderId);
        OrderDto.Main dto = OrderDto.Main.of(order);
        if (!dto.getUserId().equals(SessionUtils.getUserId(request))) {
            throw new InvalidAccessException();
        }
        return CommonResponse.success(dto);
    }

    @PostMapping("/{orderId}/pay")
    public CommonResponse<OrderDto.Main> payOrder(HttpServletRequest request, @PathVariable Long orderId) {
        Order payedOrder = orderService.pay(SessionUtils.getUserId(request), orderId);
        return CommonResponse.success(
                OrderDto.Main.of(payedOrder)
        );
    }

    @DeleteMapping("/{orderId}/pay")
    public CommonResponse<OrderDto.Main> cancelPayment(HttpServletRequest request, @PathVariable Long orderId) {
        Order canceledOrder = orderService.cancelPayment(SessionUtils.getUserId(request), orderId);
        return CommonResponse.success(
                OrderDto.Main.of(canceledOrder)
        );
    }

    @PostMapping("/items/complete")
    public CommonResponse<List<OrderItemDto.Main>> completeOneOrderItem(
            HttpServletRequest request, @RequestParam(name = "itemId") List<Long> itemIds) {
        List<OrderItem> orderItems = orderService.completeOrderItems(SessionUtils.getUserId(request), itemIds);
        return CommonResponse.success(
                orderItems.stream()
                        .map(OrderItemDto.Main::of)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("/{orderId}/complete")
    public CommonResponse<OrderDto.Main> completeOrder(HttpServletRequest request, @PathVariable Long orderId) {
        Order order = orderService.completeOrder(SessionUtils.getUserId(request), orderId);
        return CommonResponse.success(OrderDto.Main.of(order));
    }

    @PostMapping("/items/refund")
    public CommonResponse<List<OrderItemDto.Main>> refundOneOrderItem(
            HttpServletRequest request, @RequestParam(name = "itemIds") List<Long> itemIds) {
        List<OrderItem> refundOrderItems = orderService.refundOrderItems(SessionUtils.getUserId(request), itemIds);
        return CommonResponse.success(
                refundOrderItems.stream()
                        .map(OrderItemDto.Main::of)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("/{orderId}/refund")
    public CommonResponse<List<OrderItemDto.Main>> refundOrderItems(
            HttpServletRequest request, @PathVariable Long orderId, @RequestParam("itemIds") List<Long> orderItemIdList) {
        List<OrderItem> result = orderService.refundOrderItems(SessionUtils.getUserId(request), orderId, orderItemIdList);
        return CommonResponse.success(
                result.stream()
                        .map(OrderItemDto.Main::of)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("/items/exchange")
    public CommonResponse<List<OrderItemDto.Main>> exchangeOneOrderItem(
            HttpServletRequest request, @RequestParam(name = "itemId") List<Long> itemIds) {

        List<OrderItem> orderItems = orderService.exchangeOrderItems(SessionUtils.getUserId(request), itemIds);
        return CommonResponse.success(
                orderItems.stream()
                        .map(OrderItemDto.Main::of)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("/{orderId}/exchange")
    public CommonResponse<List<OrderItemDto.Main>> exchangeOrderItems(
            HttpServletRequest request, @PathVariable Long orderId, @RequestParam(name = "itemIds") List<Long> orderItemIdList) {
        List<OrderItem> result = orderService.exchangeOrderItems(SessionUtils.getUserId(request), orderId, orderItemIdList);
        return CommonResponse.success(
                result.stream()
                        .map(OrderItemDto.Main::of)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("/{orderId}/cancel/delivery")
    public CommonResponse<List<OrderItemDto.Main>> registerCancelDelivery(HttpServletRequest request,
                                                                          @PathVariable Long orderId,
                                                                          @RequestParam(name = "itemId") List<Long> orderItemIdList,
                                                                          @RequestBody OrderDto.RegisterDeliveryRequest registerRequest) {
        List<OrderItem> result = orderService.registerCancelDelivery(
                SessionUtils.getUserId(request), orderId, orderItemIdList,
                registerRequest.getDeliveryCode(), registerRequest.getDeliveryProvider());
        return CommonResponse.success(
                result.stream()
                        .map(OrderItemDto.Main::of)
                        .collect(Collectors.toList())
        );
    }

    /* manager only path */

    @PostMapping("/items/delivery")
    public CommonResponse<List<OrderItemDto.Main>> registerOneDelivery(
            @RequestParam(name = "itemId") List<Long> itemIds, @RequestBody OrderDto.RegisterDeliveryRequest registerRequest) {
        List<OrderItem> orderItems = orderService.registerDeliveryOne(
                itemIds,
                registerRequest.getDeliveryCode(),
                registerRequest.getDeliveryProvider()
        );
        return CommonResponse.success(
                orderItems.stream()
                        .map(OrderItemDto.Main::of)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("/{orderId}/delivery")
    public CommonResponse<OrderDto.Main> registerAllDelivery(@PathVariable Long orderId,
                                                             @RequestBody OrderDto.RegisterDeliveryRequest registerRequest) {
        Order registeredOrder = orderService.registerDeliveryAll(
                orderId,
                registerRequest.getDeliveryCode(),
                registerRequest.getDeliveryProvider()
        );
        return CommonResponse.success(
                OrderDto.Main.of(registeredOrder)
        );
    }

    @PostMapping("/items/delivery/done")
    public CommonResponse<List<OrderItemDto.Main>> deliveryDone(@RequestParam(name = "itemId") List<Long> itemIds) {
        List<OrderItem> orderItems = orderService.deliveryDone(itemIds);
        return CommonResponse.success(
                orderItems.parallelStream()
                        .map(OrderItemDto.Main::of)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("/{orderId}/cancel/complete")
    public CommonResponse<OrderDto.Main> completeCancelOrder(@PathVariable Long orderId) {
        Order order = orderService.completeAllCancel(orderId);
        return CommonResponse.success(OrderDto.Main.of(order));
    }

    @PostMapping("/items/cancel/complete")
    public CommonResponse<List<OrderItemDto.Main>> completeCancelOrderItem(@RequestParam(name = "itemId") List<Long> itemIds) {
        List<OrderItem> orderItems = orderService.completeCancel(itemIds);
        return CommonResponse.success(
                orderItems.parallelStream()
                        .map(OrderItemDto.Main::of)
                        .collect(Collectors.toList())
        );
    }
}
