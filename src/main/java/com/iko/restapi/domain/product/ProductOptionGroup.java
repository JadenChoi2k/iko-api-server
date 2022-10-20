package com.iko.restapi.domain.product;

import com.iko.restapi.common.entity.BaseTimeEntity;
import com.iko.restapi.common.exception.EntityNotFoundException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Entity
@NoArgsConstructor
public class ProductOptionGroup extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "product_option_id")
    private Long id;

    private String optionName;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductOptionItem> items = new ArrayList<>();

    @Setter
    @Column(nullable = false)
    private Boolean optional = true;

    public ProductOptionGroup(Long id, Product product, String optionName, List<ProductOptionItem> items) {
        this(product, optionName, items);
        this.id = id;
    }

    public ProductOptionGroup(Product product, String optionName, List<ProductOptionItem> items) {
        this(optionName, items);
        this.product = product;
    }

    public ProductOptionGroup(String optionName, List<ProductOptionItem> items) {
        this.optionName = optionName;
        items.forEach(item -> item.setGroup(this));
        this.items = items;
    }

    public ProductOptionGroup changeOptional(boolean value) {
        this.optional = value;
        return this;
    }

    public ProductOptionItem select(String value) {
        return items.stream()
                .filter(item -> item.getOptionValue().equals(value))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("옵션에 해당하는 값을 찾을 수 없습니다."));
    }
}
