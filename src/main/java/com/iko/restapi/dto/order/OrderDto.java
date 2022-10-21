package com.iko.restapi.dto.order;

import com.iko.restapi.domain.order.Order;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

public class OrderDto {
    /* Request */

    @Data
    public static class CreateRequest {
        private List<Long> cartItemIdList;
    }

    @Data
    public static class RegisterDeliveryRequest {
        private String deliveryCode;
        private String deliveryProvider;
    }

    public static class EditRequest {

    }

    /* Response */

    @Data
    @Builder
    public static class Main {
        private Long orderId;
        private Long userId;
        private List<OrderItemDto.Main> orderItems;

        // todo: repository 계층에서 한 방 쿼리
        public static Main of(Order order) {
            return Main.builder()
                    .orderId(order.getId())
                    .userId(order.getUser().getId())
                    .orderItems(
                            order.getOrderItems().stream()
                                    .map(OrderItemDto.Main::of)
                                    .collect(Collectors.toList())
                    )
                    .build();
        }
    }
}
