package com.iko.restapi.dto.order;

import com.iko.restapi.domain.order.Order;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDto {
    /* Request */

    @Data
    public static class CreateRequest {
        @NotEmpty
        private List<Long> cartItemIdList;
    }

    @Data
    public static class FillDeliveryInfoRequest {
        @NotEmpty
        private String recipient;
        @NotEmpty
        private String address;
        @NotEmpty
        private String zipCode;
    }

    @Data
    public static class RegisterDeliveryRequest {
        @NotEmpty
        private String deliveryCode;
        @NotEmpty
        private String deliveryProvider;
    }

    /* Response */

    @Data
    @Builder
    public static class Detail {
        private Long orderId;
        private Long userId;
        private Integer deliveryFee;
        private String recipient;
        private String address;
        private String zipCode;
        private List<OrderItemDto.Main> orderItems;

        // todo: repository 계층에서 한 방 쿼리
        public static Detail of(Order order) {
            return Detail.builder()
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
