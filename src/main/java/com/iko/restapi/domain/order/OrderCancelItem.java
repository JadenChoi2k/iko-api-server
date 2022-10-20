package com.iko.restapi.domain.order;

import com.iko.restapi.common.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderCancelItem extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "order_cancel_item_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;

    // 교환/환불일 경우 고객이 기입하는 란
    private String deliveryCode;

    private String deliveryProvider;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_cancel_state")
    private State state = State.IN_PROGRESS;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_cancel_type")
    private Type type;

    @Getter
    public enum State {
        IN_PROGRESS, DONE
    }

    @Getter
    @RequiredArgsConstructor
    public enum Type {
        PAYMENT("결제 취소"), REFUND("환불"), EXCHANGE("교환");

        private final String description;
    }

    public OrderCancelItem(OrderItem orderItem, Type type) {
        this.orderItem = orderItem;
        this.type = type;
    }
}
