package com.iko.restapi.domain.product;

import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductOptionItem> items = new ArrayList<>();

    public ProductOptionGroup(String optionName, Product product, List<ProductOptionItem> items) {
        this.optionName = optionName;
        this.product = product;
        this.items = items;
    }

    // [name, value]
    public List<List<String>> optionItems() {
        return items.stream()
                .map((item) -> List.of(item.getName(), item.getValue()))
                .collect(Collectors.toList());
    }
}
