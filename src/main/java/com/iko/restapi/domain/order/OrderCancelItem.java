package com.iko.restapi.domain.order;

import com.iko.restapi.common.entity.BaseTimeEntity;
import com.iko.restapi.common.exception.InvalidAccessException;
import com.iko.restapi.common.exception.InvalidParameterException;
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

    public void registerDelivery(String deliveryCode, String deliveryProvider) {
        if (this.type == Type.PAYMENT) {
            throw new InvalidParameterException("환불, 교환의 경우에만 배송 정보를 등록할 수 있습니다");
        }
        this.deliveryCode = deliveryCode;
        this.deliveryProvider = deliveryProvider;
    }

    public void complete() {
        if (this.state == State.DONE) {
            throw new InvalidAccessException("이미 완료된 취소 요청입니다");
        }
        this.state = State.DONE;
    }
}
