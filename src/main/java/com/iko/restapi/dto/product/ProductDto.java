package com.iko.restapi.dto.product;

import com.iko.restapi.domain.product.Product;
import com.iko.restapi.domain.product.ProductOptionGroup;
import com.iko.restapi.domain.product.ProductOptionItem;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

public class ProductDto {

    /* Response */

    @ApiModel
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PageItem {
        @ApiModelProperty(value = "아이디", example = "1616006")
        private Long id;
        @ApiModelProperty(value = "이름", example = "ベア·ブラウン")
        private String name;
        @ApiModelProperty(value = "한국어이름", allowEmptyValue = true, example = "베어 [ 브라운 ] 1개월용")
        private String nameKor;
        @ApiModelProperty(value = "타이틀", example = "ベア·ブラウン")
        private String title;
        @ApiModelProperty(value = "브랜드", example = "Layered")
        private String brand;
        @ApiModelProperty(value = "소비자가격", example = "2200")
        private Integer consumerPrice;
        @ApiModelProperty(value = "판매가", example = "1800")
        private Integer sellPrice;
        @ApiModelProperty(value = "판매 여부", example = "true")
        private Boolean selling;
        @ApiModelProperty(value = "품절 여부", example = "false")
        private Boolean soldOut;
        @ApiModelProperty(value = "기본배송비", example = "500")
        private Integer defaultDeliveryFee;
        @ApiModelProperty(value = "무료배송가격", example = "4500")
        private Integer freeDeliveryFee;
        @ApiModelProperty(value = "컬러 이름", example = "ブラウン")
        private String colorName;
        @ApiModelProperty(value = "컬러 코드", example = "#524333")
        private String colorCode;
        @ApiModelProperty(value = "이미지 1", example = "https://lenssis.jp/data/item/0001616006/7Iqk7YWU652867iM65287Jq0_1month_7IOB7IS4.jpg")
        private String image1;
        @ApiModelProperty(value = "이미지 2", example = "https://lenssis.jp/data/item/0001616006/7Iqk7YWU652867iM65287Jq0_67Kg7Iqk7Yq466qo64247IKs7KeE.jpg")
        private String image2;

        PageItem(
                Long id, String name, String nameKor, String title, String brand,
                Integer consumerPrice, Integer sellPrice, Boolean selling, Boolean soldOut,
                Integer defaultDeliveryFee, Integer freeDeliveryFee, String colorName, String colorCode,
                String image1, String image2) {
            this.id = id;
            this.name = name;
            this.nameKor = nameKor;
            this.title = title;
            this.brand = brand;
            this.consumerPrice = consumerPrice;
            this.sellPrice = sellPrice;
            this.selling = selling;
            this.soldOut = soldOut;
            this.defaultDeliveryFee = defaultDeliveryFee;
            this.freeDeliveryFee = freeDeliveryFee;
            this.colorName = colorName;
            this.colorCode = colorCode;
            this.image1 = image1;
            this.image2 = image2;
        }

        public static ProductDto.PageItem of(Product product) {
            return new ProductDto.PageItem(
                    product.getId(),
                    product.getName(),
                    product.getNameKor(),
                    product.getTitle(),
                    product.getBrand(),
                    product.getConsumerPrice(),
                    product.getSellPrice(),
                    product.getSelling(),
                    product.getSoldOut(),
                    product.getDefaultDeliveryFee(),
                    product.getFreeDeliveryFee(),
                    product.getColorName(),
                    product.getColorCode(),
                    product.getImage1(),
                    product.getImage2()
            );
        }
    }

    @ApiModel
    @Getter
    public static class Detail extends PageItem {
//        private Long id;
//        private String name;
//        private String nameKor;
//        private String title;
        private String manufacturer;
//        private String brand;
        private List<OptionGroup> options;
        @ApiModelProperty("상세 내용")
        private String description;
//        private Integer consumerPrice;
//        private Integer sellPrice;
//        private Boolean selling;
//        private Boolean soldOut;
//        private Integer defaultDeliveryFee;
//        private Integer freeDeliveryFee;
        @ApiModelProperty("seo 타이틀")
        private String seoTitle;
        @ApiModelProperty("seo 설명")
        private String seoDescription;
        @ApiModelProperty("seo 키워드")
        private String seoKeyword;
        @ApiModelProperty("seo 스탠다드")
        private String seoStandard;
        @ApiModelProperty("일반 배송 가이드")
        private String generalDeliveryGuide;
        @ApiModelProperty("빠른 배송 가이드")
        private String speedDeliveryGuide;
//        private String colorName;
//        private String colorCode;
//        private String image1;
//        private String image2;

        Detail(Long id, String name, String nameKor, String title, String manufacturer, String brand, List<OptionGroup> options, String description, Integer consumerPrice, Integer sellPrice, Boolean selling, Boolean soldOut, Integer defaultDeliveryFee, Integer freeDeliveryFee, String seoTitle, String seoDescription, String seoKeyword, String seoStandard, String generalDeliveryGuide, String speedDeliveryGuide, String colorName, String colorCode, String image1, String image2) {
            super.id = id;
            super.name = name;
            super.nameKor = nameKor;
            super.title = title;
            this.manufacturer = manufacturer;
            super.brand = brand;
            this.options = options;
            this.description = description;
            super.consumerPrice = consumerPrice;
            super.sellPrice = sellPrice;
            super.selling = selling;
            super.soldOut = soldOut;
            super.defaultDeliveryFee = defaultDeliveryFee;
            super.freeDeliveryFee = freeDeliveryFee;
            this.seoTitle = seoTitle;
            this.seoDescription = seoDescription;
            this.seoKeyword = seoKeyword;
            this.seoStandard = seoStandard;
            this.generalDeliveryGuide = generalDeliveryGuide;
            this.speedDeliveryGuide = speedDeliveryGuide;
            super.colorName = colorName;
            super.colorCode = colorCode;
            super.image1 = image1;
            super.image2 = image2;
        }

        public static Detail of(Product product) {
            return new Detail(
                    product.getId(),
                    product.getName(),
                    product.getNameKor(),
                    product.getTitle(),
                    product.getManufacturer(),
                    product.getBrand(),
                    product.getOptions().stream().map(OptionGroup::of).collect(Collectors.toList()),
                    product.getDescription(),
                    product.getConsumerPrice(),
                    product.getSellPrice(),
                    product.getSelling(),
                    product.getSoldOut(),
                    product.getDefaultDeliveryFee(),
                    product.getFreeDeliveryFee(),
                    product.getSeoTitle(),
                    product.getSeoDescription(),
                    product.getSeoKeyword(),
                    product.getSeoStandard(),
                    product.getGeneralDeliveryGuide(),
                    product.getSpeedDeliveryGuide(),
                    product.getColorName(),
                    product.getColorCode(),
                    product.getImage1(),
                    product.getImage2()
            );
        }
    }

    @ApiModel
    @Data
    static class OptionGroup {
        private Long groupId;
        private String optionName;
        private List<OptionItem> items;
        private Boolean optional;

        OptionGroup(Long groupId, String optionName, List<OptionItem> items, Boolean optional) {
            this.groupId = groupId;
            this.optionName = optionName;
            this.items = items;
            this.optional = optional;
        }

        public static OptionGroup of(ProductOptionGroup og) {
            return new OptionGroup(
                    og.getId(),
                    og.getOptionName(),
                    og.getItems().stream().map(OptionItem::of).collect(Collectors.toList()),
                    og.getOptional()
            );
        }
    }

    @ApiModel
    @Data
    static class OptionItem {
        private String name;
        private String value;
        private Integer price;

        OptionItem(String name, String value, Integer price) {
            this.name = name;
            this.value = value;
            this.price = price;
        }

        public static OptionItem of(ProductOptionItem item) {
            return new OptionItem(item.getName(), item.getOptionValue(), item.getPrice());
        }
    }
}
