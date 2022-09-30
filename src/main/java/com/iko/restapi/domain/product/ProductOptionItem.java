package com.iko.restapi.domain.product;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class ProductOptionItem {
    @Id
    @GeneratedValue
    @Column(name = "option_itme_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    private ProductOptionGroup group;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String value;

    public ProductOptionItem(ProductOptionGroup group, String name, String value) {
        this.group = group;
        this.name = name;
        this.value = value;
    }
}
