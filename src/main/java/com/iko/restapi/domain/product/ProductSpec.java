package com.iko.restapi.domain.product;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class ProductSpec {
    @Id
    @GeneratedValue
    @Column(name = "product_spec_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(length = 31)
    private String diameter;

    @Column(length = 31)
    private String graphicDiameter;

    @Column(length = 31)
    private String baseCurve;

    @Column(length = 31)
    private String availableDegree;

    @Column(length = 31)
    private String moisture;

    @Column(length = 31)
    private String duration;

    @Column(length = 31)
    private String material;

    @Column(length = 31)
    private String rewardPoints;

    @Column(length = 1023)
    private String frequency;

    public ProductSpec(Product product, String diameter, String graphicDiameter, String baseCurve, String availableDegree, String moisture, String duration, String material, String point, String frequency) {
        this.product = product;
        this.diameter = diameter;
        this.graphicDiameter = graphicDiameter;
        this.baseCurve = baseCurve;
        this.availableDegree = availableDegree;
        this.moisture = moisture;
        this.duration = duration;
        this.material = material;
        this.rewardPoints = point;
        this.frequency = frequency;
    }
}
