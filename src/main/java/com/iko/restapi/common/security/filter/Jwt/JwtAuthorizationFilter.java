package com.iko.restapi.common.security.filter.Jwt;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import com.iko.restapi.common.security.PrincipalDetails;
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
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) 
			throws IOException, ServletException, AuthenticationCredentialsNotFoundException {

        // JWT 추출
		String token = jwtTokenProvider.getJwt();

		// 토큰이 없음: 401 이어야 하는데 403
		if(null == token) {
			log.info("LoginFailed: token null");
			doFilter(request, response, chain);
			return;
		}
		
		// 토큰이 올바르지 않음: 401 이어야 하는데 403
		Claims claim;
		try {
			claim = jwtTokenProvider.getClaim(token);
		} catch (Exception e) {
			doFilter(request, response, chain);
			return;
		}
		
		// 토큰의 유효기간이 끝남: 401 이어야 하는데 403
		if (!jwtTokenProvider.validateToken(claim)) {
			doFilter(request, response, chain);
			return;
		}
		
		// 올바른 refreshToken을 받은 경우: 만료된 acc날려서 거절한번 당하고
		// refresh 보내왔으니 acc토큰 발급해서 헤더에 넣어주고 인증처리 
		if("refreshToken".equals(claim.get("type").toString())) {
			Authentication authentication = jwtTokenProvider.getAuthentication(token);
			var principalDetails = (PrincipalDetails) authentication.getPrincipal();
			String renewAccessToken = jwtTokenProvider.createAccToken(principalDetails.getUsername(), principalDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);
			response.addHeader("renewAccessToken", renewAccessToken);
			doFilter(request, response, chain);
			return;
		}
		
		// 올바른 accessToken을 받은 경우
		Authentication authentication = jwtTokenProvider.getAuthentication(token);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	    doFilter(request, response, chain);
	}
	
}