package com.iko.restapi.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS) // jwt 이후 STATELESS로 유지
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                // .logout() 구현하기
                .authorizeRequests()
                    .antMatchers("/api/v1/product", "/api/v1/product/**")
                    .permitAll();
    }
}
