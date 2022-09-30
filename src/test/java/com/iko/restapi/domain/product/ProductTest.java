package com.iko.restapi.domain.product;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void create() {
        String description = "<p style=\"text-align:center;\" align=\"center\"><img src=\"https://lenssis.jp/data/editor/2207/280e22c7b7f041136ff80f96ea5d80a2_1656654022_792.jpg\" title=\"280e22c7b7f041136ff80f96ea5d80a2_1656654022_792.jpg\" alt=\"280e22c7b7f041136ff80f96ea5d80a2_1656654022_792.jpg\"><br style=\"clear:both;\"><img src=\"https://lenssis.jp/data/editor/2207/280e22c7b7f041136ff80f96ea5d80a2_1656654024_5328.jpg\" title=\"280e22c7b7f041136ff80f96ea5d80a2_1656654024_5328.jpg\" alt=\"280e22c7b7f041136ff80f96ea5d80a2_1656654024_5328.jpg\"><br style=\"clear:both;\">&nbsp;</p>";
        String seoDescription = "ステラブラウンはどんな肌のトーンにもおすすめカラコンです。。ナチュラルグレー カラコン。最も危?なUV-B98%もカット、最大含水率68%の高含水率を持つ。";

        var opts1 = List.of(
                new ProductOptionItem("파랑", "blue"),
                new ProductOptionItem("빨강", "red"),
                new ProductOptionItem("초록", "green")
        );
        var og1 = new ProductOptionGroup("color", opts1);
        var opts2 = List.of(
                new ProductOptionItem("M", "M"),
                new ProductOptionItem("L", "L"),
                new ProductOptionItem("XL", "XL")
        );
        var og2 = new ProductOptionGroup("size", opts2);

        var product = new Product(
                "ベア·ブラウン", "베어 [ 브라운 ] 1개월용", "ベア·ブラウン",
                "Made in Korea", "Layered", List.of(og1, og2), description, 2200, 1800, true, false,
                500, 4500, "ブラウン, イエベ,フチなし, ふちなし", "ステラブラウン | 楽かわレンシス(lenssis)",
                seoDescription, "", "https://lenssis.jp/shop/0001616006", "ブラウン",
                "#524333", null, null, "https://lenssis.jp/data/item/0001616006/7Iqk7YWU652867iM65287Jq0_1month_7IOB7IS4.jpg",
                "https://lenssis.jp/data/item/0001616006/7Iqk7YWU652867iM65287Jq0_67Kg7Iqk7Yq466qo64247IKs7KeE.jpg"
        );

    }
}