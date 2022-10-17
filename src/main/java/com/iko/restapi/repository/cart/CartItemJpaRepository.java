package com.iko.restapi.repository.cart;

import com.iko.restapi.domain.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemJpaRepository extends JpaRepository<CartItem, Long> {
    @Query("select item from CartItem item where item.user.id = :userId")
    List<CartItem> findAllItemsByUserId(Long userId);

    @Query("delete from CartItem item where item.user.id = :userId")
    void deleteAllItemsByUserId(Long userId);
}
