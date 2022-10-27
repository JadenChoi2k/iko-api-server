package com.iko.restapi.common.config;

import com.iko.restapi.common.security.logout.JwtLogoutHandler;
import com.iko.restapi.common.security.logout.LogoutTokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
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
    
 // authenticationManager를 Bean 등록합니다.
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
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
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt 이후 STATELESS로 유지
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic()
                // .logout() 구현하기
                .and()
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
                	.antMatchers("/api/v1/login")
                		.permitAll()
                    .antMatchers("/api/v1/cart/**")
                        .authenticated()
                    .antMatchers("/api/v1/user/me/**")
                        .authenticated()
                    .antMatchers("/refreshToken")
                    	.permitAll()
                    .anyRequest().permitAll();
    }
}