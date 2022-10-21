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
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderItem> orderItems = new ArrayList<>();

    public Order(User user) {
        this.user = user;
    }

    public Integer wholePrice() {
        return orderItems.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(Integer::sum)
                .orElseThrow(() -> new SystemException("오류 발생: 가격 누락"));
    }
}
