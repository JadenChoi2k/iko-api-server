package com.iko.restapi.dto;

import com.iko.restapi.domain.product.Product;
import com.iko.restapi.domain.product.ProductOptionGroup;
import com.iko.restapi.domain.product.ProductOptionItem;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

public class ProductDto {

    /* Response */

    @Getter
    public static class PageItem {
        private Long id;
        private String name;
        private String nameKor;
        private String title;
        private String brand;
        private Integer consumerPrice;
        private Integer sellPrice;
        private Boolean selling;
        private Boolean soldOut;
        private Integer defaultDeliveryFee;
        private Integer freeDeliveryFee;
        private String colorName;
        private String colorCode;
        private String image1;
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

    @Getter
    public static class Detail {
        private Long id;
        private String name;
        private String nameKor;
        private String title;
        private String manufacturer;
        private String brand;
        private List<OptionGroup> options;
        private String description;
        private Integer consumerPrice;
        private Integer sellPrice;
        private Boolean selling;
        private Boolean soldOut;
        private Integer defaultDeliveryFee;
        private Integer freeDeliveryFee;
        private String seoTitle;
        private String seoDescription;
        private String seoKeyword;
        private String seoStandard;
        private String generalDeliveryGuide;
        private String speedDeliveryGuide;
        private String colorName;
        private String colorCode;
        private String image1;
        private String image2;

        Detail(Long id, String name, String nameKor, String title, String manufacturer, String brand, List<OptionGroup> options, String description, Integer consumerPrice, Integer sellPrice, Boolean selling, Boolean soldOut, Integer defaultDeliveryFee, Integer freeDeliveryFee, String seoTitle, String seoDescription, String seoKeyword, String seoStandard, String generalDeliveryGuide, String speedDeliveryGuide, String colorName, String colorCode, String image1, String image2) {
            this.id = id;
            this.name = name;
            this.nameKor = nameKor;
            this.title = title;
            this.manufacturer = manufacturer;
            this.brand = brand;
            this.options = options;
            this.description = description;
            this.consumerPrice = consumerPrice;
            this.sellPrice = sellPrice;
            this.selling = selling;
            this.soldOut = soldOut;
            this.defaultDeliveryFee = defaultDeliveryFee;
            this.freeDeliveryFee = freeDeliveryFee;
            this.seoTitle = seoTitle;
            this.seoDescription = seoDescription;
            this.seoKeyword = seoKeyword;
            this.seoStandard = seoStandard;
            this.generalDeliveryGuide = generalDeliveryGuide;
            this.speedDeliveryGuide = speedDeliveryGuide;
            this.colorName = colorName;
            this.colorCode = colorCode;
            this.image1 = image1;
            this.image2 = image2;
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
