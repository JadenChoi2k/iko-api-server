package com.iko.restapi.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

@Configuration
public class SwaggerConfig {
    public static final String PRODUCT_TAG = "Product";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .useDefaultResponseMessages(false)
                .select()
                    .apis(RequestHandlerSelectors.basePackage("com.iko.restapi.controller"))
                    .paths(PathSelectors.any())
                    .build()
                .tags(
                        createTag(PRODUCT_TAG, "제품")
                )
                .apiInfo(apiInfo());
    }



    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("IKO REST API Server")
                .description("<h2>IKO 웹서버 API 문서입니다</h2>")
                .version("0.1.0")
                .build();
    }

    private Tag createTag(String name, String description) {
        return new Tag(name, description);
    }

//    // based on swagger 3.0
//    @Bean
//    public OpenAPI openAPI() {
//        return new OpenAPI().components(new Components())
//                .tags(List.of(createTag(PRODUCT_TAG, "제품")))
//                .info(info());
//    }
//
//    private Info info() {
//        return new Info()
//                .title("IKO REST API Server")
//                .description("<h2>IKO 웹서버 API 문서입니다</h2>")
//                .version("0.1.0");
//    }
}
