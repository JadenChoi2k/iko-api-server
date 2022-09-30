package com.iko.restapi.domain.product;

import com.iko.restapi.common.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "product_id")
    private Long id;

    @Column(nullable = false)
    private String productCode; // 일단은 id랑 동일하게 맞출 예정

    @Column(nullable = false)
    private String name;

    private String nameKor;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 31)
    private String manufacturer;

    @Column(nullable = false, length = 31)
    private String brand;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductOptionGroup> options = new ArrayList<>();

    @Column(nullable = false, length = 65535)
    private String detail;

    @Column(nullable = false)
    private Integer consumerPrice;

    @Column(nullable = false)
    private Integer sellPrice;

    @Column(nullable = false)
    private Boolean selling;

    @Column(nullable = false)
    private Boolean soldOut;

    @Column(nullable = false)
    private Integer defaultDeliveryFee;

    @Column(nullable = false)
    private Integer freeDeliveryFee;

    /**
     * TODO:요약설명 내용 추가 (여기)
     */

    // 검색어 기반 우선순위 쿼리(나중에)
    private String searchKeyword;

    // seo group (entity로 나눠도 될 것 같습니다)
    private String seoTitle;
    private String seoDescription;
    private String seoKeyword;
    private String seoStandard;

    // color
    @Column(length = 15)
    private String colorName;
    // hex code
    @Column(length = 7)
    private String colorCode;

    private String generalDeliveryGuide;
    private String speedDeliveryGuide;

    private String image1;
    private String image2;

    public Product(
            String productCode, String name, String nameKor, String title, String manufacturer, String brand,
            List<ProductOptionGroup> options, String detail, Integer consumerPrice, Integer sellPrice,
            Boolean selling, Boolean soldOut, Integer defaultDeliveryFee, Integer freeDeliveryFee, String searchKeyword,
            String seoTitle, String seoDescription, String seoKeyword, String seoStandard, String colorName, String colorCode,
            String generalDeliveryGuide, String speedDeliveryGuide, String image1, String image2) {
        this.productCode = productCode;
        this.name = name;
        this.nameKor = nameKor;
        this.title = title;
        this.manufacturer = manufacturer;
        this.brand = brand;
        this.options = options;
        this.detail = detail;
        this.consumerPrice = consumerPrice;
        this.sellPrice = sellPrice;
        this.selling = selling;
        this.soldOut = soldOut;
        this.defaultDeliveryFee = defaultDeliveryFee;
        this.freeDeliveryFee = freeDeliveryFee;
        this.searchKeyword = searchKeyword;
        this.seoTitle = seoTitle;
        this.seoDescription = seoDescription;
        this.seoKeyword = seoKeyword;
        this.seoStandard = seoStandard;
        this.colorName = colorName;
        this.colorCode = colorCode;
        this.generalDeliveryGuide = generalDeliveryGuide;
        this.speedDeliveryGuide = speedDeliveryGuide;
        this.image1 = image1;
        this.image2 = image2;
    }

    /**
     * 옵션 목록을 반환한다.
     * @return [{optionGroupName: [[name(String), value(String)] 쌍]}]
     */
    public List<Map<String, List<List<String>>>> getOptions() {
        return options.stream()
                .map(group -> Map.of(group.getOptionName(), group.optionItems()))
                .collect(Collectors.toList());
    }
}
