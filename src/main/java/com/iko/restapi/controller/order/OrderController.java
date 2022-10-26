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
    public CommonResponse<OrderDto.Detail> createOrder(@RequestBody OrderDto.CreateRequest createRequest) {
        Order order = orderService.createOrder(createRequest.getCartItemIdList());
        return CommonResponse.success(OrderDto.Detail.of(order));
    }

    @GetMapping
    public PaginationResponse<OrderDto.Detail> findAllOrder(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        List<Order> result = orderService.findAllOrder(page, size);
        return PaginationResponse.success(
                page,
                "date",
                result.stream()
                        .map(OrderDto.Detail::of)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{orderId}")
    public CommonResponse<OrderDto.Detail> findOneOrder(HttpServletRequest request, @PathVariable Long orderId) {
        Order order = orderService.findOneOrder(orderId);
        OrderDto.Detail dto = OrderDto.Detail.of(order);
        if (!dto.getUserId().equals(SessionUtils.getUserId(request))) {
            throw new InvalidAccessException();
        }
        return CommonResponse.success(dto);
    }

    @PostMapping("/{orderId}/delivery/fill")
    public CommonResponse<String> fillDeliveryInfo(@PathVariable Long orderId,
                                                   @RequestBody OrderDto.FillDeliveryInfoRequest fillRequest) {
        orderService.fillDeliveryInfo(
                orderId,
                fillRequest.getRecipient(), fillRequest.getAddress(), fillRequest.getZipCode());
        return CommonResponse.ok();
    }

    @PostMapping("/{orderId}/pay")
    public CommonResponse<String> payOrder(@PathVariable Long orderId) {
        Order payedOrder = orderService.pay(orderId);
        return CommonResponse.ok();
    }

    @DeleteMapping("/{orderId}/pay")
    public CommonResponse<String> cancelPayment(@PathVariable Long orderId) {
        Order canceledOrder = orderService.cancelPayment(orderId);
        return CommonResponse.ok();
    }

    @PostMapping("/items/complete")
    public CommonResponse<List<OrderItemDto.Main>> completeOneOrderItem(@RequestParam(name = "itemId") List<Long> itemIds) {
        List<OrderItem> orderItems = orderService.completeOrderItems(itemIds);
        return CommonResponse.success(
                orderItems.stream()
                        .map(OrderItemDto.Main::of)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("/{orderId}/complete")
    public CommonResponse<OrderDto.Detail> completeOrder(@PathVariable Long orderId) {
        Order order = orderService.completeOrder(orderId);
        return CommonResponse.success(OrderDto.Detail.of(order));
    }

    @PostMapping("/items/refund")
    public CommonResponse<List<OrderItemDto.Main>> refundOneOrderItem(@RequestParam(name = "itemIds") List<Long> itemIds) {
        List<OrderItem> refundOrderItems = orderService.refundOrderItems(itemIds);
        return CommonResponse.success(
                refundOrderItems.stream()
                        .map(OrderItemDto.Main::of)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("/{orderId}/refund")
    public CommonResponse<List<OrderItemDto.Main>> refundOrderItems(
            @PathVariable Long orderId, @RequestParam("itemIds") List<Long> orderItemIdList) {
        List<OrderItem> result = orderService.refundOrderItems(orderId, orderItemIdList);
        return CommonResponse.success(
                result.stream()
                        .map(OrderItemDto.Main::of)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("/items/exchange")
    public CommonResponse<List<OrderItemDto.Main>> exchangeOneOrderItem(
            @RequestParam(name = "itemId") List<Long> itemIds) {

        List<OrderItem> orderItems = orderService.exchangeOrderItems(itemIds);
        return CommonResponse.success(
                orderItems.stream()
                        .map(OrderItemDto.Main::of)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("/{orderId}/exchange")
    public CommonResponse<List<OrderItemDto.Main>> exchangeOrderItems(
            @PathVariable Long orderId, @RequestParam(name = "itemIds") List<Long> orderItemIdList) {
        List<OrderItem> result = orderService.exchangeOrderItems(orderId, orderItemIdList);
        return CommonResponse.success(
                result.stream()
                        .map(OrderItemDto.Main::of)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("/{orderId}/cancel/delivery")
    public CommonResponse<List<OrderItemDto.Main>> registerCancelDelivery(
            @PathVariable Long orderId, @RequestParam(name = "itemId") List<Long> orderItemIdList,
            @RequestBody OrderDto.RegisterDeliveryRequest registerRequest) {
        List<OrderItem> result = orderService.registerCancelDelivery(
                orderId, orderItemIdList,
                registerRequest.getDeliveryCode(), registerRequest.getDeliveryProvider());
        return CommonResponse.success(
                result.stream()
                        .map(OrderItemDto.Main::of)
                        .collect(Collectors.toList())
        );
    }

    /* manager only path */

    @PostMapping("/{orderId}/ready/product")
    public CommonResponse<OrderDto.Detail> readyOrderProduct(@PathVariable Long orderId) {
        Order order = orderService.readyOrderProduct(orderId);
        return CommonResponse.success(OrderDto.Detail.of(order));
    }

    @PostMapping("/{orderId}/ready/delivery")
    public CommonResponse<OrderDto.Detail> readyOrderDelivery(@PathVariable Long orderId) {
        Order order = orderService.readyOrderDelivery(orderId);
        return CommonResponse.success(OrderDto.Detail.of(order));
    }

//    @PostMapping("/items/delivery")
//    public CommonResponse<List<OrderItemDto.Main>> registerOneDelivery(
//            @RequestParam(name = "itemId") List<Long> itemIds, @RequestBody OrderDto.RegisterDeliveryRequest registerRequest) {
//        List<OrderItem> orderItems = orderService.registerDeliveryOne(
//                itemIds,
//                registerRequest.getDeliveryCode(),
//                registerRequest.getDeliveryProvider()
//        );
//        return CommonResponse.success(
//                orderItems.stream()
//                        .map(OrderItemDto.Main::of)
//                        .collect(Collectors.toList())
//        );
//    }

    @PostMapping("/{orderId}/delivery")
    public CommonResponse<OrderDto.Detail> registerAllDelivery(@PathVariable Long orderId,
                                                               @RequestBody OrderDto.RegisterDeliveryRequest registerRequest) {
        Order registeredOrder = orderService.registerDeliveryAll(
                orderId,
                registerRequest.getDeliveryCode(),
                registerRequest.getDeliveryProvider()
        );
        return CommonResponse.success(
                OrderDto.Detail.of(registeredOrder)
        );
    }

    @PostMapping("/{orderId}/delivery/done")
    public CommonResponse<OrderDto.Detail> orderDeliveryDone(@PathVariable Long orderId) {
        Order order = orderService.deliveryDone(orderId);
        return CommonResponse.success(OrderDto.Detail.of(order));
    }

//    @PostMapping("/items/delivery/done")
//    public CommonResponse<List<OrderItemDto.Main>> deliveryDone(@RequestParam(name = "itemId") List<Long> itemIds) {
//        List<OrderItem> orderItems = orderService.deliveryDone(itemIds);
//        return CommonResponse.success(
//                orderItems.stream()
//                        .map(OrderItemDto.Main::of)
//                        .collect(Collectors.toList())
//        );
//    }

    @PostMapping("/{orderId}/cancel/complete")
    public CommonResponse<OrderDto.Detail> completeCancelOrder(@PathVariable Long orderId) {
        Order order = orderService.completeAllCancel(orderId);
        return CommonResponse.success(OrderDto.Detail.of(order));
    }

    @PostMapping("/items/cancel/complete")
    public CommonResponse<List<OrderItemDto.Main>> completeCancelOrderItem(@RequestParam(name = "itemId") List<Long> itemIds) {
        List<OrderItem> orderItems = orderService.completeCancel(itemIds);
        return CommonResponse.success(
                orderItems.stream()
                        .map(OrderItemDto.Main::of)
                        .collect(Collectors.toList())
        );
    }
}
