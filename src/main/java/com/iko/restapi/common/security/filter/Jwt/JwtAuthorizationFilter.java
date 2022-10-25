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

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

		if(null == token) {
			log.info("LoginFailed: token null");
			doFilter(request, response, chain);
			return;
		}

		Claims claim;
		try {
			claim = jwtTokenProvider.getClaim(token);
		} catch (Exception e) {
			doFilter(request, response, chain);
			return;
		}
		if (!jwtTokenProvider.validateToken(claim)) {
			doFilter(request, response, chain);
			return;
		}
		
		if("refreshToken".equals(claim.get("type").toString())) {
			// acc 토큰 요청 (401에러)
			// 여기로 나가면 403에러
			doFilter(request, response, chain);
			return;
		}
		
		// 올바른 토큰+유효기간
		Authentication authentication = jwtTokenProvider.getAuthentication(token);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	    doFilter(request, response, chain);
	}

}