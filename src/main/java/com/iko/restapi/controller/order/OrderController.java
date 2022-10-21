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

    // only manager endpoint
    @PostMapping("/item/{orderItemId}/delivery")
    public CommonResponse<OrderItemDto.Main> registerOneDelivery(@PathVariable Long orderItemId,
                                                                 @RequestBody OrderDto.RegisterDeliveryRequest registerRequest) {
        OrderItem orderItem = orderService.registerDeliveryOne(
                orderItemId,
                registerRequest.getDeliveryCode(),
                registerRequest.getDeliveryProvider()
        );
        return CommonResponse.success(
                OrderItemDto.Main.of(orderItem)
        );
    }

    // only manager endpoint
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

    @PostMapping("/item/{orderItemId}/delivery/done")
    public CommonResponse<OrderItemDto.Main> deliveryDone(@PathVariable Long orderItemId) {
        OrderItem orderItem = orderService.deliveryDone(orderItemId);
        return CommonResponse.success(
                OrderItemDto.Main.of(orderItem)
        );
    }
}
