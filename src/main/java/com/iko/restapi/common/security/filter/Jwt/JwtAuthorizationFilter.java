package com.iko.restapi.common.security.filter.Jwt;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import com.iko.restapi.common.security.provider.JwtTokenProvider;
import com.iko.restapi.repository.user.UserJpaRepository;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
	private final AuthenticationManager authenticationManager;	
	private final JwtTokenProvider jwtTokenProvider;
	private final UserJpaRepository userRepository;
	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserJpaRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        // JWT 추출
		String token = jwtTokenProvider.getJwt();

        // JWT 기간 검증 후 authentication 생성 및 주입
		// TODO: 토큰없을때, 토큰 만료됐을때, 리프레시 토큰 만료됐을때, 토큰잘못됐을때 나누기
        if ((token != null) && jwtTokenProvider.validateToken(token)) {
        	Authentication authentication = jwtTokenProvider.getAuthentication(token);
        	SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        
        // JWT 기간 검증 후 만료됐으면 refreshToken 요청
        
        // JWT 리프레시 토큰 만료되거나 토큰 없거나 이상하면 로그인페이지
        
        
        
	    doFilter(request, response, chain);
	}

}