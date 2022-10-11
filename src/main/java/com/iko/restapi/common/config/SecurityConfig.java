package com.iko.restapi.common.config;

import com.iko.restapi.common.security.SessionAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS) // jwt 이후 STATELESS로 유지
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                // .logout() 구현하기
                .logout()
                    .logoutUrl("/logout")
                .and()
                .addFilter(new SessionAuthenticationFilter(authenticationManager(), passwordEncoder()))
                .authorizeRequests()
                    .antMatchers("/api/v1/user/edit", "/api/v1/user/pwEdit")
                        .authenticated()
                    .anyRequest().permitAll();
    }
}
