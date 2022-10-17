package com.iko.restapi.domain.product;

import com.iko.restapi.common.exception.EntityNotFoundException;
import com.iko.restapi.common.exception.InvalidParameterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

public class ProductTest {

    public static Product createProduct() {
        String description = "<p style=\"text-align:center;\" align=\"center\"><img src=\"https://lenssis.jp/data/editor/2207/280e22c7b7f041136ff80f96ea5d80a2_1656654022_792.jpg\" title=\"280e22c7b7f041136ff80f96ea5d80a2_1656654022_792.jpg\" alt=\"280e22c7b7f041136ff80f96ea5d80a2_1656654022_792.jpg\"><br style=\"clear:both;\"><img src=\"https://lenssis.jp/data/editor/2207/280e22c7b7f041136ff80f96ea5d80a2_1656654024_5328.jpg\" title=\"280e22c7b7f041136ff80f96ea5d80a2_1656654024_5328.jpg\" alt=\"280e22c7b7f041136ff80f96ea5d80a2_1656654024_5328.jpg\"><br style=\"clear:both;\">&nbsp;</p>";
        String seoDescription = "ステラブラウンはどんな肌のトーンにもおすすめカラコンです。。ナチュラルグレー カラコン。最も危?なUV-B98%もカット、最大含水率68%の高含水率を持つ。";

        var opts1 = List.of(
                new ProductOptionItem(11L, "파랑", "blue", 0),
                new ProductOptionItem(22L, "빨강", "red", 0),
                new ProductOptionItem(33L, "초록", "green", 0)
        );
        var og1 = new ProductOptionGroup(111L, null, "color", opts1);
        var opts2 = List.of(
                new ProductOptionItem(44L, "M", "M", 0),
                new ProductOptionItem(55L, "L", "L", 0),
                new ProductOptionItem(66L, "XL", "XL", 100)
        );
        var og2 = new ProductOptionGroup(222L, null, "size", opts2);

        var product = new Product(
                "ベア·ブラウン", "베어 [ 브라운 ] 1개월용", "ベア·ブラウン",
                "Made in Korea", "Layered", List.of(og1, og2), description, 2200, 1800, true, false,
                500, 4500, "ブラウン, イエベ,フチなし, ふちなし", "ステラブラウン | 楽かわレンシス(lenssis)",
                seoDescription, "", "https://lenssis.jp/shop/0001616006", "ブラウン",
                "#524333", null, null, "https://lenssis.jp/data/item/0001616006/7Iqk7YWU652867iM65287Jq0_1month_7IOB7IS4.jpg",
                "https://lenssis.jp/data/item/0001616006/7Iqk7YWU652867iM65287Jq0_67Kg7Iqk7Yq466qo64247IKs7KeE.jpg"
        );
        return product;
    }

    @DisplayName("제품 생성")
    @Test
    void create() {
        // given
        String description = "<p style=\"text-align:center;\" align=\"center\"><img src=\"https://lenssis.jp/data/editor/2207/280e22c7b7f041136ff80f96ea5d80a2_1656654022_792.jpg\" title=\"280e22c7b7f041136ff80f96ea5d80a2_1656654022_792.jpg\" alt=\"280e22c7b7f041136ff80f96ea5d80a2_1656654022_792.jpg\"><br style=\"clear:both;\"><img src=\"https://lenssis.jp/data/editor/2207/280e22c7b7f041136ff80f96ea5d80a2_1656654024_5328.jpg\" title=\"280e22c7b7f041136ff80f96ea5d80a2_1656654024_5328.jpg\" alt=\"280e22c7b7f041136ff80f96ea5d80a2_1656654024_5328.jpg\"><br style=\"clear:both;\">&nbsp;</p>";
        String seoDescription = "ステラブラウンはどんな肌のトーンにもおすすめカラコンです。。ナチュラルグレー カラコン。最も危?なUV-B98%もカット、最大含水率68%の高含水率を持つ。";

        var opts1 = List.of(
                new ProductOptionItem("파랑", "blue", 0),
                new ProductOptionItem("빨강", "red", 0),
                new ProductOptionItem("초록", "green", 0)
        );
        var og1 = new ProductOptionGroup("color", opts1);
        var opts2 = List.of(
                new ProductOptionItem("M", "M", 0),
                new ProductOptionItem("L", "L", 0),
                new ProductOptionItem("XL", "XL", 1000)
        );
        var og2 = new ProductOptionGroup("size", opts2);

        // when
        var product = new Product(
                "ベア·ブラウン", "베어 [ 브라운 ] 1개월용", "ベア·ブラウン",
                "Made in Korea", "Layered", List.of(og1, og2), description, 2200, 1800, true, false,
                500, 4500, "ブラウン, イエベ,フチなし, ふちなし", "ステラブラウン | 楽かわレンシス(lenssis)",
                seoDescription, "", "https://lenssis.jp/shop/0001616006", "ブラウン",
                "#524333", null, null, "https://lenssis.jp/data/item/0001616006/7Iqk7YWU652867iM65287Jq0_1month_7IOB7IS4.jpg",
                "https://lenssis.jp/data/item/0001616006/7Iqk7YWU652867iM65287Jq0_67Kg7Iqk7Yq466qo64247IKs7KeE.jpg"
        );

        // then
        assertThat(product.getDescription()).isEqualTo(description);
        assertThat(product.getSeoDescription()).isEqualTo(seoDescription);
        assertThat(product.getOptions().get(0)).isEqualTo(og1);
        assertThat(product.getOptions().get(1)).isEqualTo(og2);
        product.getOptions().stream()
                .forEach((group) -> assertThat(group.getProduct()).isEqualTo(product));
    }

    @DisplayName("옵션 선택")
    @Test
    void selectOptions() {
        // given
        Product product = createProduct();
        // when
        Map<ProductOptionGroup, ProductOptionItem> selected = product.selectOptions(Map.of("color", "blue", "size", "L"));
        // then
        assertThat(selected.size()).isEqualTo(2);
        for (var entry: selected.entrySet()) {
            assertThat(selected.get(entry.getKey())).isEqualTo(entry.getValue());
        }
    }

    @DisplayName("옵션 선택 오류 - 존재하지 않는 그룹 이름")
    @Test
    void selectOptionsGroupNameNotFoundException() {
        // given
        Product product = createProduct();
        // when, then
        assertThatThrownBy(() -> product.selectOptions(Map.of("nono", "value")))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("옵션 선택 오류 - 존재하지 않는 옵션값")
    @Test
    void selectOptionsItemValueNotFoundException() {
        // given
        Product product = createProduct();
        // when, then
        assertThatThrownBy(() -> product.selectOptions(Map.of("color", "nono")))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("제품 가격 구하기")
    @Test
    void wholePrice() {
        // given
        Product product = createProduct();
        Map<ProductOptionGroup, ProductOptionItem> select = product.selectOptions(Map.of("color", "blue", "size", "XL"));
        // when
        int price = product.wholePrice(new ArrayList<>(select.values()));
        // then
        assertThat(price).isEqualTo(product.getSellPrice() + 100);
    }
}