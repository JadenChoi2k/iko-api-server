package com.iko.restapi.repository.order;

import com.iko.restapi.common.exception.EntityNotFoundException;
import com.iko.restapi.domain.cart.CartItem;
import com.iko.restapi.domain.cart.CartItemOptionItem;
import com.iko.restapi.domain.order.Order;
import com.iko.restapi.domain.order.OrderItem;
import com.iko.restapi.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderCustomRepositoryImpl implements OrderCustomRepository {
    private final EntityManager em;

    @Override
    public Order createOrderByUserIdAndCartIdList(Long userId, List<Long> cartIdList) throws EntityNotFoundException {
        List<CartItem> cartItemList = em.createQuery("select item from CartItem item" +
                        " where item.id in :cartIdList and item.user.id = :userId", CartItem.class)
                .setParameter("cartIdList", cartIdList)
                .setParameter("userId", userId)
                .getResultList();
        if (cartItemList.isEmpty()) {
            throw new EntityNotFoundException("카트 아이템이 없습니다");
        }
        var user = cartItemList.get(0).getUser();
        var order = new Order(user);
        var orderItems = cartItemList.stream()
                .map((cartItem) -> OrderItem.create(
                        order,
                        cartItem.getProduct(),
                        cartItem.getCount(),
                        cartItem.getOptions().stream()
                                .map(CartItemOptionItem::getOptionItem)
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
        // order, orderItem 생성 완료, 관계 주입 및 필드 초기화
        order.setOrderItems(orderItems);
        order.decideDeliveryFee();
        em.persist(order);
        cartItemList.forEach(em::remove);
        return order;
    }

    @Override
    public Order createOrderByUserAndCartIdList(User user, List<Long> cartIdList) {
        List<CartItem> cartItemList = em.createQuery("select item from CartItem item" +
                        " where item.id in :cartIdList and item.user = :user", CartItem.class)
                .setParameter("cartIdList", cartIdList)
                .setParameter("user", user)
                .getResultList();
        if (cartItemList.isEmpty()) {
            throw new EntityNotFoundException("카트 아이템이 없습니다");
        }
        var order = new Order(user);
        var orderItems = cartItemList.stream()
                .map((cartItem) -> OrderItem.create(
                        order,
                        cartItem.getProduct(),
                        cartItem.getCount(),
                        cartItem.getOptions().stream()
                                .map(CartItemOptionItem::getOptionItem)
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
        // order, orderItem 생성 완료, 관계 주입 및 필드 초기화
        order.setOrderItems(orderItems);
        order.decideDeliveryFee();
        em.persist(order);
        cartItemList.forEach(em::remove);
        return order;
    }
}
