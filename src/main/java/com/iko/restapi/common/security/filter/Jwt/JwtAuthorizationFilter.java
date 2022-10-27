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

		// 토큰이 없음: 401
		if(null == token) {
			log.info("token not in Authorization");
			doFilter(request, response, chain);
			return;
		}
		
		// 토큰이 올바르지 않음 or 유효기간 만료: 401
		Claims claim;
		try {
			claim = jwtTokenProvider.getClaim(token, "authKey");
		} catch (Exception e) {
			doFilter(request, response, chain);
			return;
		}
		
		// refreshToken관련은 컨트롤러로 뺀다. 
//		if("refreshToken".equals(claim.get("type").toString())) {
//			Authentication authentication = jwtTokenProvider.getAuthentication(token);
//			var principalDetails = (PrincipalDetails) authentication.getPrincipal();
//			String renewAccessToken = jwtTokenProvider.createAccToken(principalDetails.getUsername(), principalDetails.getAuthorities());
//			SecurityContextHolder.getContext().setAuthentication(authentication);
//			response.addHeader("renewAccessToken", renewAccessToken);
//			doFilter(request, response, chain);
//			return;
//		}
		
		// 토큰 종류가 엑세스 토큰이 아니면 401
		if(!"accessToken".equals(claim.get("type").toString())) {
			doFilter(request, response, chain);
			return;
		}
		
		// 올바른 accessToken을 받은 경우
		Authentication authentication = jwtTokenProvider.getAuthentication(token, "authKey");
		SecurityContextHolder.getContext().setAuthentication(authentication);
	    doFilter(request, response, chain);
	}
	
}