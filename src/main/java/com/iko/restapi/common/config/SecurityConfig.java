package com.iko.restapi.common.config;

import com.iko.restapi.common.security.filter.LogoutSuccessHandlerImpl;
import com.iko.restapi.common.security.auth.session.SessionAuthenticationFilter;
import com.iko.restapi.common.security.auth.session.SessionAuthorizationFilter;
import com.iko.restapi.repository.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.Rfc6265CookieProcessor;
import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserJpaRepository userRepository;

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
                    .logoutUrl("/logout")
                    .logoutSuccessHandler(new LogoutSuccessHandlerImpl())
                .and()
                .addFilter(new SessionAuthenticationFilter(authenticationManager(), passwordEncoder()))
                .addFilter(new SessionAuthorizationFilter(authenticationManager(), userRepository))
                .authorizeRequests()
                    .antMatchers("/api/v1/user/**")
                        .permitAll()
                    .antMatchers("/api/v1/cart/**")
                        .authenticated()
                    .antMatchers("/api/v1/user/me/**")
                        .authenticated()
                    .anyRequest().permitAll();
    }
}
