package com.iko.restapi.domain.cart;

import com.iko.restapi.common.exception.InvalidParameterException;
import com.iko.restapi.domain.product.Product;
import com.iko.restapi.domain.product.ProductOptionItem;
import com.iko.restapi.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem {
    @Id
    @GeneratedValue
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "cartItem")
    private List<CartItemOptionItem> options = new ArrayList<>();

    @Column(nullable = false)
    private Integer count = 1;

    // for test
    public CartItem(Long id, User user, Product product, List<CartItemOptionItem> options, Integer count) {
        this.id = id;
        this.user = user;
        this.product = product;
        this.options = options;
        this.count = count;
    }

    public static CartItem create(User user, Product product, List<ProductOptionItem> selectedOptions, Integer count) throws InvalidParameterException {
        var cartItem = new CartItem();
        cartItem.user = user;
        cartItem.product = product;
        product.validateSelected(selectedOptions);
        cartItem.options = selectedOptions.stream()
                .map((opt) -> CartItemOptionItem.create(cartItem, opt))
                .collect(Collectors.toList());
        cartItem.count = count;
        return cartItem;
    }
}
