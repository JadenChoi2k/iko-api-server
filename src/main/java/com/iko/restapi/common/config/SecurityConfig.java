package com.iko.restapi.common.config;

import com.iko.restapi.common.security.filter.LogoutSuccessHandlerImpl;
import com.iko.restapi.common.security.filter.SessionAuthenticationFilter;
import com.iko.restapi.common.security.filter.SessionAuthorizationFilter;
import com.iko.restapi.repository.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserJpaRepository userRepository;

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
                    .logoutSuccessHandler(new LogoutSuccessHandlerImpl())
                .and()
                .addFilter(new SessionAuthenticationFilter(authenticationManager(), passwordEncoder()))
                .addFilter(new SessionAuthorizationFilter(authenticationManager(), userRepository))
                .authorizeRequests()
                    .antMatchers("/api/v1/user/join")
                        .permitAll()
                    .antMatchers("/api/v1/user/**")
                        .authenticated()
                    .anyRequest().permitAll();
    }
}
