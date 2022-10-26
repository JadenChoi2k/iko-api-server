package com.iko.restapi.domain.order;

import com.iko.restapi.domain.product.ProductOptionItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItemOptionItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_option_item")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_item_id")
    private ProductOptionItem optionItem;

    public OrderItemOptionItem(OrderItem orderItem, ProductOptionItem optionItem) {
        this.orderItem = orderItem;
        this.optionItem = optionItem;
    }
}
