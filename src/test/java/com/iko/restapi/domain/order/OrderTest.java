package com.iko.restapi.domain.order;

import com.iko.restapi.common.exception.InvalidAccessException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.iko.restapi.domain.product.ProductTest.createProduct;
import static com.iko.restapi.domain.user.UserTest.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTest {
    public static Order createOrder() {
        var user = createUser();
        var order = new Order(user);
        var product = createProduct();
        var orderItem = OrderItem.create(
                order,
                product,
                2,
                List.of(
                        product.getOptions().get(0).getItems().get(0),
                        product.getOptions().get(1).getItems().get(0)
                )
        );
        order.setOrderItems(List.of(orderItem));
        order.fillDeliveryInfo("홍길동", "중앙로 12번길 302호", "123456");
        return order;
    }

    static OrderItem getOrderItem() {
        return createOrder().getOrderItems().get(0);
    }

    @Test
    void create() {
        // given, when
        var user = createUser();
        var order = new Order(user);
        var product = createProduct();
        var orderItem = OrderItem.create(
                order,
                product,
                2,
                List.of(
                        product.getOptions().get(0).getItems().get(0),
                        product.getOptions().get(1).getItems().get(0)
                )
        );
        order.setOrderItems(List.of(orderItem));
        // then
        assertThat(order.getUser()).isEqualTo(user);
        assertThat(order.getOrderItems().get(0)).isEqualTo(orderItem);
        assertThat(orderItem.getProduct()).isEqualTo(product);
        assertThat(orderItem.getCount()).isEqualTo(2);
        assertThat(orderItem.getSelectedOptions()).hasSize(2);
    }

    @Test
    void wholePrice() {
        // given
        Order order = createOrder();
        // when
        Integer expected = order.getOrderItems().get(0).getTotalPrice();
        Integer actual = order.wholeProductPrice();
        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void pay() {
        // given
        OrderItem orderItem = getOrderItem();
        // when
        orderItem.pay();
        // then
        assertThat(orderItem.getState()).isEqualTo(OrderItem.State.PAYED);
    }

    @Test
    void cancelPayment() {
        // given
        OrderItem orderItem = getOrderItem();
        // when
        orderItem.pay();
        orderItem.cancelPayment();
        // then
        assertThat(orderItem.getState()).isEqualTo(OrderItem.State.CANCEL);
        assertThat(orderItem.getOrderCancelItem()).isNotNull();
        assertThat(orderItem.getOrderCancelItem().getType()).isEqualTo(OrderCancelItem.Type.PAYMENT);
        assertThat(orderItem.getOrderCancelItem().getState()).isEqualTo(OrderCancelItem.State.IN_PROGRESS);
    }

    @Test
    void cancelPaymentComplete() {
        // given
        OrderItem orderItem = getOrderItem();
        // when
        orderItem.pay();
        orderItem.cancelPayment();
        orderItem.getOrderCancelItem().complete();
        // then
        assertThat(orderItem.getOrderCancelItem().getState()).isEqualTo(OrderCancelItem.State.DONE);
    }

    @Test
    void registerDelivery() {
        // given
        OrderItem orderItem = getOrderItem();
        orderItem.pay();
        // when
        orderItem.readyProduct();
        orderItem.readyDelivery();
        orderItem.registerDelivery("123456", "한진");
        // then
        assertThat(orderItem.getState()).isEqualTo(OrderItem.State.IN_DELIVERY);
    }
    
    @Test
    void registerDeliveryException() {
        // given
        OrderItem orderItem = getOrderItem();
        // when, then;
        assertThatThrownBy(() -> orderItem.registerDelivery("123456", "한진"))
                .isInstanceOf(InvalidAccessException.class);
    }

    @Test
    void doneDelivery() {
        // given
        OrderItem orderItem = getOrderItem();
        orderItem.pay();
        orderItem.readyProduct();
        orderItem.readyDelivery();
        orderItem.registerDelivery("123456", "한진");
        // when
        orderItem.doneDelivery();
        // then
        assertThat(orderItem.getState()).isEqualTo(OrderItem.State.DELIVERY_DONE);
    }

    @Test
    void doneDeliveryException() {
        // given
        OrderItem orderItem = getOrderItem();
        orderItem.pay();
        // when, then
        assertThatThrownBy(orderItem::doneDelivery)
                .isInstanceOf(InvalidAccessException.class);
    }

    @Test
    void completeOrder() {
        // given
        OrderItem orderItem = getOrderItem();
        orderItem.pay();
        orderItem.readyProduct();
        orderItem.readyDelivery();
        orderItem.registerDelivery("123456", "한진");
        orderItem.doneDelivery();
        // when
        orderItem.completeOrder();
        // then
        assertThat(orderItem.getState()).isEqualTo(OrderItem.State.COMPLETE);
    }

    @Test
    void refundOrder() {
        // given
        OrderItem orderItem = getOrderItem();
        orderItem.pay();
        orderItem.readyProduct();
        orderItem.readyDelivery();
        orderItem.registerDelivery("123456", "한진");
        // when
        OrderCancelItem orderCancelItem = orderItem.refund();
        // then
        assertThat(orderItem.isCanceled()).isTrue();
        assertThat(orderItem.getOrderCancelItem()).isEqualTo(orderCancelItem);
        assertThat(orderCancelItem.getType()).isEqualTo(OrderCancelItem.Type.REFUND);
        assertThat(orderCancelItem.getState()).isEqualTo(OrderCancelItem.State.IN_PROGRESS);
    }

    @Test
    void refundOrderComplete() {
        // given
        OrderItem orderItem = getOrderItem();
        orderItem.pay();
        orderItem.readyProduct();
        orderItem.readyDelivery();
        orderItem.registerDelivery("123456", "한진");
        // when
        OrderCancelItem orderCancelItem = orderItem.refund();
        orderCancelItem.complete();
        // then
        assertThat(orderCancelItem.getState()).isEqualTo(OrderCancelItem.State.DONE);
    }

    @Test
    void exchangeOrder() {
        // given
        OrderItem orderItem = getOrderItem();
        orderItem.pay();
        orderItem.readyProduct();
        orderItem.readyDelivery();
        orderItem.registerDelivery("123456", "한진");
        // when
        OrderCancelItem orderCancelItem = orderItem.exchange();
        // then
        assertThat(orderItem.isCanceled()).isTrue();
        assertThat(orderItem.getOrderCancelItem()).isEqualTo(orderCancelItem);
        assertThat(orderCancelItem.getType()).isEqualTo(OrderCancelItem.Type.EXCHANGE);
        assertThat(orderCancelItem.getState()).isEqualTo(OrderCancelItem.State.IN_PROGRESS);
    }

    @Test
    void exchangeOrderComplete() {
        // given
        OrderItem orderItem = getOrderItem();
        orderItem.pay();
        orderItem.readyProduct();
        orderItem.readyDelivery();
        orderItem.registerDelivery("123456", "한진");
        // when
        OrderCancelItem orderCancelItem = orderItem.exchange();
        orderCancelItem.complete();
        // then
        assertThat(orderCancelItem.getState()).isEqualTo(OrderCancelItem.State.DONE);
    }
}
