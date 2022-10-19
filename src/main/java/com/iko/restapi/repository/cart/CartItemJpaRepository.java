package com.iko.restapi.repository.cart;

import com.iko.restapi.domain.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public interface CartItemJpaRepository extends JpaRepository<CartItem, Long> {
    @Query("select item from CartItem item where item.user.id = :userId")
    List<CartItem> findAllItemsByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("delete from CartItem item where item.user.id = :userId")
    void deleteAllItemsByUserId(@Param("userId") Long userId);
}
