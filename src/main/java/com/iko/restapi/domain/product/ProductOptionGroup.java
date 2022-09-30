package com.iko.restapi.domain.product;

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
public class ProductOptionGroup {
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
}
