package com.iko.restapi.domain.product;

import com.iko.restapi.common.entity.BaseTimeEntity;
import com.iko.restapi.common.exception.EntityNotFoundException;
import com.iko.restapi.common.exception.InvalidParameterException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "product_id")
    private Long id;

//    @Column(nullable = false)
//    private String productCode; // 일단 id를 리턴

    @Column(nullable = false)
    private String name;

    private String nameKor; // 국제화 기능이 안 들어갈지 의문입니다..

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 31)
    private String manufacturer;

    @Column(nullable = false, length = 31)
    private String brand;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductOptionGroup> options = new ArrayList<>();

    @Column(nullable = false, length = 65535)
    private String description;

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

    public String productCode() {
        return id.toString();
    }

    public Product(
            String name, String nameKor, String title, String manufacturer, String brand,
            List<ProductOptionGroup> options, String detail, Integer consumerPrice, Integer sellPrice,
            Boolean selling, Boolean soldOut, Integer defaultDeliveryFee, Integer freeDeliveryFee, String searchKeyword,
            String seoTitle, String seoDescription, String seoKeyword, String seoStandard, String colorName, String colorCode,
            String generalDeliveryGuide, String speedDeliveryGuide, String image1, String image2) {
        this.name = name;
        this.nameKor = nameKor;
        this.title = title;
        this.manufacturer = manufacturer;
        this.brand = brand;
        options.forEach(og -> og.setProduct(this));
        this.options = options;
        this.description = detail;
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

    public int wholePrice(List<ProductOptionItem> selected) {
        return selected.stream().reduce(0, (x, y) -> x + y.getPrice(), Integer::sum);
    }

    public void validateSelected(Map<ProductOptionGroup, ProductOptionItem> selected) {
        options.forEach(og -> {
            if (og.getOptional()) {
                return;
            }
            selected.entrySet().stream()
                    .filter(entry -> entry.getKey().equals(og.getOptionName()))
                    .findAny()
                    .orElseThrow(() -> new InvalidParameterException("필수 옵션을 선택해주세요"));
        });
    }

    public void validateSelected(List<ProductOptionItem> selected) {
        options.forEach(og -> {
            if (og.getOptional()) {
                return;
            }
            selected.stream().filter(item -> item.getGroup().equals(og))
                    .findAny()
                    .orElseThrow(() -> {
                        throw new InvalidParameterException("필수 옵션을 선택해주세요");
                    });
        });
    }

    /**
     * 옵션을 선택하는 메서드
     * @param select 옵션 선택 (optionGroupName, optionValue 쌍)
     * @return 선택된 옵션 목록 (optionGroup, optionItem 쌍)
     * @throws InvalidParameterException : 필수 선택값 미선택 시 발생
     */
    public Map<ProductOptionGroup, ProductOptionItem> selectOptions(Map<String, String> select) {
        Map<ProductOptionGroup, ProductOptionItem> selected = new HashMap<>();
        for (String groupName: select.keySet()) {
            var optionGroup = options.stream()
                    .filter(og -> og.getOptionName().equals(groupName))
                    .findAny()
                    .orElseThrow(() -> new EntityNotFoundException("해당 옵션을 찾을 수 없습니다"));
            var optionItem = optionGroup.select(select.get(groupName));
            selected.put(optionGroup, optionItem);
        }
        // optional = false인 group들 선택되었는지 검증
        validateSelected(selected);
        return selected;
    }
}
