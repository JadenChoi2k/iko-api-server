package com.iko.restapi.domain.cart;

import com.iko.restapi.domain.product.Product;
import com.iko.restapi.domain.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static com.iko.restapi.domain.product.ProductTest.createProduct;
import static com.iko.restapi.domain.user.UserTest.createUser;

public class CartTest {

    public static CartItem createCartItem() {
        User user = createUser();
        Product product = createProduct();
        CartItem cartItem = CartItem.create(
                user,
                product,
                new ArrayList<>(
                        product.selectOptions(Map.of("color", "blue", "size", "M"))
                                .values()
                ),
                1
        );
        return cartItem;
    }

    @Test
    public void create() throws Exception {
        // given
        User user = createUser();
        Product product = createProduct();
        CartItem cartItem = CartItem.create(
                user,
                product,
                new ArrayList<>(
                        product.selectOptions(Map.of("color", "blue", "size", "M"))
                                .values()
                ),
                1
        );
        // when, then
        Assertions.assertThat(cartItem.getUser()).isEqualTo(user);
        Assertions.assertThat(cartItem.getProduct()).isEqualTo(product);
    }
}
