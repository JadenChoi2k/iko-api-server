package com.iko.restapi.dto.cart;

import com.iko.restapi.domain.cart.CartItem;
import com.iko.restapi.domain.cart.CartItemOptionItem;
import com.iko.restapi.dto.product.ProductDto;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

public class CartDto {
    
    /* Request */
    
    @Data
    public static class CreateRequest {
        @NotNull(message = "제품 아이디 누락: productId")
        private Long productId;
        @NotNull(message = "옵션 아이디 리스트 누락: optionIdList")
        private List<Long> optionIdList;
        @NotNull(message = "상품 개수 누락: count")
        @Min(value = 1, message = "상품 개수는 최소 1입니다")
        private Integer count;
    }

    @Data
    public static class EditRequest {
        private List<Long> optionIdList;
        @Min(value = 1, message = "상품 개수는 최소 1입니다")
        private Integer count;
    }
    
    /* Response */
    
    @Data
    public static class Main {
        private Long cartItemId;
        private Long productId;
        private String productName;
        private String productImageUrl;
        private List<ProductDto.OptionItem> selectedOptionList;
        private Integer price;
        private Integer count;

        public Main(Long cartItemId, Long productId, String productName, String productImageUrl, List<ProductDto.OptionItem> selectedOptionList, Integer price, Integer count) {
            this.cartItemId = cartItemId;
            this.productId = productId;
            this.productName = productName;
            this.productImageUrl = productImageUrl;
            this.selectedOptionList = selectedOptionList;
            this.price = price;
            this.count = count;
        }

        public static Main of(CartItem item) {
            var productOptionItems = item.getOptions().stream()
                    .map(CartItemOptionItem::getOptionItem)
                    .collect(Collectors.toList());
            return new Main(
                    item.getId(),
                    item.getProduct().getId(),
                    item.getProduct().getName(),
                    item.getProduct().getImage1(),
                    productOptionItems.stream()
                            .map(ProductDto.OptionItem::of)
                            .collect(Collectors.toList()),
                    item.getProduct().wholePrice(productOptionItems),
                    item.getCount()
            );
        }
    }
}
