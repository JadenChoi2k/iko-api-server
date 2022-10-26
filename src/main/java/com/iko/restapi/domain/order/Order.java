package com.iko.restapi.domain.order;

import com.iko.restapi.common.entity.BaseTimeEntity;
import com.iko.restapi.common.exception.SystemException;
import com.iko.restapi.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Table(name = "orders")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Setter
    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(nullable = false)
    private Integer deliveryFee;
    // 배송 정보
    @Column(length = 63)
    private String recipient; // 수령인
    private String address; // 주소
    @Column(length = 15)
    private String zipCode; // 우편번호

    public Order(User user) {
        this.user = user;
    }

    public Integer wholeProductPrice() {
        return orderItems.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(Integer::sum)
                .orElseThrow(() -> new SystemException("오류 발생: 가격 누락"));
    }

    // 초기화하면서 의무적으로 불러줘야 합니다
    public void decideDeliveryFee() {
        if (wholeProductPrice() >= OrderConst.MIN_FREE_DELIVERY) {
            this.deliveryFee = 0;
        } else {
            this.deliveryFee = OrderConst.DEFAULT_DELIVERY;
        }
    }

    // PRE_ORDER -> PRE_PAYMENT
    public void fillDeliveryInfo(String recipient, String address, String zipCode) {
        orderItems.forEach(OrderItem::prePayment);
        this.recipient = recipient;
        this.address = address;
        this.zipCode = zipCode;
    }
}
