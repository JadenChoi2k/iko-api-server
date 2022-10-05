package com.iko.restapi.controller.product;

import com.iko.restapi.common.config.SwaggerConfig;
import com.iko.restapi.common.response.CommonResponse;
import com.iko.restapi.common.response.PaginationResponse;
import com.iko.restapi.dto.ProductDto;
import com.iko.restapi.service.product.ProductService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Api(tags = SwaggerConfig.PRODUCT_TAG, produces = "application/json")
@RequestMapping("/api/v1/product")
@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "제품 페이징", description = "제품(product)을 페이징한다.")
    @GetMapping
    public PaginationResponse<ProductDto.PageItem> products(
            @Parameter(description = "페이지넘버", example = "0") @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "페이징사이즈", example = "0") @RequestParam(defaultValue = "20") Integer size,
            // null -> recommend
            // date_desc / date_asc
            // price_desc / price_asc
            @Parameter(
                    description = "정렬 순서 - '{date/price}_{asc/desc}' 형식 입력",
                    example = "date_desc")
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

    @Operation(summary = "제품 상세", description = "제품 상세 내용을 GET 요청한다.")
    @GetMapping("/{productId}")
    public CommonResponse<ProductDto.Detail> product(@Parameter(description = "제품 아이디", example = "1616109") @PathVariable Long productId) {
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
