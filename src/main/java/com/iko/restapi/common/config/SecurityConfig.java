package com.iko.restapi.common.config;

import com.iko.restapi.common.security.logout.JwtLogoutHandler;
import com.iko.restapi.common.security.logout.LogoutTokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.iko.restapi.common.security.filter.LogoutSuccessHandlerImpl;
import com.iko.restapi.common.security.filter.Jwt.JwtAuthenticationFilter;
import com.iko.restapi.common.security.filter.Jwt.JwtAuthorizationFilter;
import com.iko.restapi.common.security.provider.JwtTokenProvider;
import com.iko.restapi.repository.user.UserJpaRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserJpaRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final LogoutTokenService logoutTokenService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsSource() {
        var corsConfiguration = new CorsConfiguration();

        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedOrigin("http://localhost:3000");
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS) // jwt 이후 STATELESS로 유지
                .and()
                .cors()
                .configurationSource(corsSource())
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                // .logout() 구현하기
                .logout()
                    .addLogoutHandler(new JwtLogoutHandler(logoutTokenService, jwtTokenProvider))
                    .logoutUrl("/logout")
                    .logoutSuccessHandler(new LogoutSuccessHandlerImpl())
                .and()
//                .addFilter(new SessionAuthenticationFilter(authenticationManager(), passwordEncoder()))
//                .addFilter(new SessionAuthorizationFilter(authenticationManager(), userRepository))
                .addFilterBefore(new JwtAuthenticationFilter(authenticationManager(), jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthorizationFilter(authenticationManager(), userRepository, jwtTokenProvider, logoutTokenService),
                        BasicAuthenticationFilter.class)
                .authorizeRequests()
                    .antMatchers("/api/v1/user/**")
                        .permitAll()
                    .antMatchers("/api/v1/cart/**")
                        .authenticated()
                    .antMatchers("/api/v1/order/**")
                        .authenticated()
                    .antMatchers(HttpMethod.POST, sellerAndAdminPostPath())
                        .hasAnyRole("ROLE_SELLER", "ROLE_ADMIN")
                    .antMatchers("/api/v1/user/me/**")
                        .authenticated()
                    .antMatchers("/auth/refresh")
                    	.permitAll()
                    .anyRequest().permitAll();
    }

    private String[] sellerAndAdminPostPath() {
        List<String> ret = new ArrayList<>();

        // order
        String orderPath = "/api/v1/order";
        ret.add(orderPath + "/**/ready/**");
        ret.add(orderPath + "/**/delivery");
        ret.add(orderPath + "/**/delivery/done");
        ret.add(orderPath + "/**/cancel/complete");

        return ret.toArray(new String[0]);
    }
}
