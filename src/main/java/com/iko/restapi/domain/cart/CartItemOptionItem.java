package com.iko.restapi.domain.cart;

import com.iko.restapi.domain.product.ProductOptionItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItemOptionItem {
    @Id
    @GeneratedValue
    @Column(name = "cart_item_option_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_item_id")
    private CartItem cartItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_item_id")
    private ProductOptionItem optionItem;

    // for test
    public CartItemOptionItem(Long id, CartItem cartItem, ProductOptionItem optionItem) {
        this.id = id;
        this.cartItem = cartItem;
        this.optionItem = optionItem;
    }

    public static CartItemOptionItem create(CartItem cartItem, ProductOptionItem optionItem) {
        var obj = new CartItemOptionItem();
        obj.cartItem = cartItem;
        obj.optionItem = optionItem;
        return obj;
    }
}
