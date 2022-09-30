package com.iko.restapi.domain.product;

import com.iko.restapi.common.exception.InvalidParameterException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class ProductOptionItem {
    @Id
    @GeneratedValue
    @Column(name = "option_item_id")
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "option_group_id")
    private ProductOptionGroup group;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String optionValue;

    @Column(nullable = false)
    private Integer price = 0;

    public ProductOptionItem(ProductOptionGroup group, String name, String value) {
        this(name, value);
        this.group = group;
    }

    public ProductOptionItem(String name, String value) {
        this.name = name;
        this.optionValue = value;
    }

    public ProductOptionItem changePrice(int price) {
        if (price < 0) {
            throw new InvalidParameterException("옵션 가격은 0 이상만 가능합니다");
        }
        this.price = price;
        return this;
    }
}
