package com.iko.restapi.dto.order;

import com.iko.restapi.domain.order.OrderItem;
import com.iko.restapi.domain.order.OrderItemOptionItem;
import com.iko.restapi.dto.product.ProductDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

public class OrderItemDto {
    /* Request */



    /* Response */

    @Data
    @Builder
    public static class Main {
        private Long orderId;
        private Long orderItemId;
        private Long productId;
        private Integer count;
        private Integer totalPrice;
        private OrderItem.State state;
        private List<ProductDto.OptionItem> selectedOptions;
        private String deliveryCode;
        private String deliveryProvider;

        // todo: repository 계층에서 한방 쿼리로 해결하기
        public static Main of(OrderItem orderItem) {
            return Main.builder()
                    .orderId(orderItem.getOrder().getId())
                    .orderItemId(orderItem.getId())
                    .productId(orderItem.getProduct().getId())
                    .count(orderItem.getCount())
                    .totalPrice(orderItem.getTotalPrice())
                    .state(orderItem.getState())
                    .selectedOptions(
                            orderItem.getSelectedOptions().stream()
                                    .map(OrderItemOptionItem::getOptionItem)
                                    .map(ProductDto.OptionItem::of)
                                    .collect(Collectors.toList())
                    )
                    .deliveryCode(orderItem.getDeliveryCode())
                    .deliveryProvider(orderItem.getDeliveryProvider())
                    .build();
        }
    }
}
