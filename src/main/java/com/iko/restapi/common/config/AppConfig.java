package com.iko.restapi.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.iko.restapi.common.interceptor.AuthenticationInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AppConfig implements WebMvcConfigurer {
    private final AuthenticationInterceptor authenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
        		.excludePathPatterns("/login")
        		.excludePathPatterns("/api/v1/user/join")
        		.excludePathPatterns("/api/v1/user/exists/*")
        		.excludePathPatterns("/api/v1/user/{userId}")
        		.excludePathPatterns("/api/v1/product/*");
    }
}
