package com.iko.restapi.controller.product;

import com.iko.restapi.common.response.CommonResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/product")
@RestController
public class ProductController {
    @GetMapping("/")
    public CommonResponse products(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(defaultValue = "date") String orderBy
            ) {
        return CommonResponse.ok();
    }
}
