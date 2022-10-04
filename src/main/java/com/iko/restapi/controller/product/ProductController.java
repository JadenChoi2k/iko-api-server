package com.iko.restapi.controller.product;

import com.iko.restapi.common.response.CommonResponse;
import com.iko.restapi.common.response.PaginationResponse;
import com.iko.restapi.dto.ProductDto;
import com.iko.restapi.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RequestMapping("/api/v1/product")
@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public PaginationResponse products(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            // null -> recommend
            // date_desc / date_asc
            // price_desc / price_asc
            @RequestParam(name = "order_by", required = false) String orderBy
            ) {
        return PaginationResponse.success(
                page,
                orderBy,
                productService.products(page, size, orderBy).stream()
                        .map(ProductDto.PageItem::of)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{productId}")
    public CommonResponse product(@PathVariable Long productId) {
        return CommonResponse.success(
                ProductDto.Detail.of(productService.findById(productId))
        );
    }

    /* 관리자 접근 path */

//    @PostMapping
//    public CommonResponse createProduct() {
//        제품 생성
//    }

//    @PatchMapping("/{productId}")
//    public CommonResponse editProduct(@PathVariable Long productId) {
//        제품 수정
//    }

//    @DeleteMapping("/{productId}")
//    public CommonResponse editProduct(@PathVariable Long productId) {
//        제품 삭제
//    }
}
