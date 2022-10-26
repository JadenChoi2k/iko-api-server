package com.iko.restapi.domain.order;

import com.iko.restapi.common.entity.BaseTimeEntity;
import com.iko.restapi.common.exception.InvalidAccessException;
import com.iko.restapi.domain.product.Product;
import com.iko.restapi.domain.product.ProductOptionItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private Integer count;

    // 최종 가격
    @Column(nullable = false)
    private Integer totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_state", nullable = false)
    private State state = State.PRE_ORDER;

    // state = CANCEL일 때만 fetch
    @OneToOne(mappedBy = "orderItem", fetch = FetchType.LAZY)
    private OrderCancelItem orderCancelItem;

    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemOptionItem> selectedOptions = new ArrayList<>();

    private String deliveryCode;

    private String deliveryProvider;

    @Getter
    @RequiredArgsConstructor
    public enum State {
        PRE_ORDER("주문 전"), PRE_PAYMENT("결제 전"), PAYED("결제 완료"), READY_PRODUCT("상품 준비 중"),
        READY_DELIVERY("배송 준비 중"), IN_DELIVERY("배송 중"), CANCEL("취소"),
        DELIVERY_DONE("배송 완료"), COMPLETE("주문 확정"), ERROR("오류 발생");
        private final String description;
    }

    public static OrderItem create(Order order, Product product, Integer count, List<ProductOptionItem> selectedOptions) {
        var orderItem = new OrderItem();

        orderItem.order = order;
        orderItem.product = product;
        orderItem.count = count;
        orderItem.selectedOptions = selectedOptions.stream()
                .map((opt) -> new OrderItemOptionItem(orderItem, opt))
                .collect(Collectors.toList());
        orderItem.totalPrice = product.wholePrice(selectedOptions);
        orderItem.state = State.PRE_ORDER;

        return orderItem;
    }

    public boolean isCanceled() {
        return this.state == State.CANCEL;
    }
    
    public void readyProduct() {
        if (this.state != State.PAYED) {
            throw new InvalidAccessException("결제 완료 후에만 접근할 수 있습니다");
        }
        this.state = State.READY_PRODUCT;
    }
    
    public void readyDelivery() {
        if (this.state != State.IN_DELIVERY) {
            throw new InvalidAccessException("상품 준비 중에만 접근할 수 있습니다");
        }
        this.state = State.READY_DELIVERY;
    }

    // 배송 정보 입력 후에만 접근
    public void prePayment() {
        if (this.state != State.PRE_ORDER) {
            throw new InvalidAccessException("주문 정보가 이미 입력되었습니다");
        }
        this.state = State.PRE_PAYMENT;
    }

    // 결제 완료 후에만 접근
    public void pay() {
        if (this.state != State.PRE_PAYMENT) {
            throw new InvalidAccessException("결제 전이 아닙니다");
        }
        this.state = State.PAYED;
    }

    public void registerDelivery(String deliveryCode, String deliveryProvider) {
        if (this.state != State.READY_DELIVERY) {
            throw new InvalidAccessException("배송 준비 상태에서만 배송 정보를 등록할 수 있습니다");
        }
        this.deliveryCode = deliveryCode;
        this.deliveryProvider = deliveryProvider;
        this.state = State.IN_DELIVERY;
    }

    public void doneDelivery() {
        if (this.state != State.IN_DELIVERY) {
            throw new InvalidAccessException("배송 중이 아닙니다");
        }
        this.state = State.DELIVERY_DONE;
    }

    public void completeOrder() {
        if (this.state != State.DELIVERY_DONE) {
            throw new InvalidAccessException("배송 완료 상태에서만 주문확정할 수 있습니다");
        }
        this.state = State.COMPLETE;
    }

    public OrderCancelItem cancelPreOrder() {
        if (this.state != State.PRE_ORDER) {
            throw new InvalidAccessException("결제 전 상태에서면 주문 취소를 할 수 있습니다");
        }
        this.state = State.CANCEL;
        return this.orderCancelItem = new OrderCancelItem(this, OrderCancelItem.Type.PRE_ORDER);
    }

    public OrderCancelItem cancelPayment() {
        if (this.state != State.PAYED) {
            throw new InvalidAccessException("결제 완료 상태에서만 결제를 취소할 수 있습니다");
        }
        this.state = State.CANCEL;
        return this.orderCancelItem = new OrderCancelItem(this, OrderCancelItem.Type.PAYMENT);
    }
    
    public OrderCancelItem refund() {
        if (this.state != State.IN_DELIVERY && this.state != State.DELIVERY_DONE) {
            throw new InvalidAccessException("배송 중이거나 배송 완료 상태에서만 환불할 수 있습니다");
        }
        this.state = State.CANCEL;
        return this.orderCancelItem = new OrderCancelItem(this, OrderCancelItem.Type.REFUND);
    }

    public OrderCancelItem exchange() {
        if (this.state != State.IN_DELIVERY && this.state != State.DELIVERY_DONE) {
            throw new InvalidAccessException("배송 중이거나 배송 완료 상태에서만 교환할 수 있습니다");
        }
        this.state = State.CANCEL;
        return this.orderCancelItem = new OrderCancelItem(this, OrderCancelItem.Type.EXCHANGE);
    }
}
