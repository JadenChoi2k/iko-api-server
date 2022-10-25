package com.iko.restapi.common.security.provider;

import java.util.Base64;
import java.util.Collection;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.iko.restapi.common.exception.BaseException;
import com.iko.restapi.common.exception.ErrorCode;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;

//토큰을 생성하고 검증하는 클래스입니다.
//해당 컴포넌트는 필터클래스에서 사전 검증을 거칩니다.
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
 private String authKey = "ikoSecretAuthKey";
 private String refreshKey = "ikoSecretRefreshKey";

 // 토큰 유효시간 
 private long authValidTime = 30 * 60 * 1000L; //30분
 private long refreshValidDay = 14 * 24 * 60 * 60 * 1000L; // 14일

 private final UserDetailsService userDetailsService;

 // 객체 초기화, secretKey를 Base64로 인코딩한다.
 @PostConstruct
 protected void init() {
	 authKey = Base64.getEncoder().encodeToString(authKey.getBytes());
	 refreshKey = Base64.getEncoder().encodeToString(refreshKey.getBytes());
 }

 // JWT 토큰 생성 
 public String createAccToken(String userPk, Collection<? extends GrantedAuthority> roles) {
     Claims claims = Jwts.claims().setSubject(userPk); // JWT payload 에 저장되는 정보단위, 보통 여기서 user를 식별하는 값을 넣는다.
     claims.put("roles", roles.stream().findFirst()); // 정보는 key / value 쌍으로 저장된다.
     claims.put("type", "accessToken");
     Date now = new Date();
     return Jwts.builder()
             .setClaims(claims) // 정보 저장
             .setIssuedAt(now) // 토큰 발행 시간 정보
             .setExpiration(new Date(now.getTime() + authValidTime)) // set Expire Time
             .signWith(SignatureAlgorithm.HS256, authKey)  // 사용할 암호화 알고리즘과
             // signature 에 들어갈 secret값 세팅
             .compact();
 }
 
 public String createRefreshToken(String userPk, Collection<? extends GrantedAuthority> roles) {
     Claims claims = Jwts.claims().setSubject(userPk); 
     claims.put("roles", roles.stream().findFirst());
     claims.put("type", "refreshToken");
     Date now = new Date();
     return Jwts.builder()
             .setClaims(claims) 
             .setIssuedAt(now) 
             .setExpiration(new Date(now.getTime() + refreshValidDay))
             .signWith(SignatureAlgorithm.HS256, refreshKey) 
             .compact();
 }
 
 // JWT 토큰 가져오기
 public String getJwt(){
     HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
     return request.getHeader("Authorization");
 }
 // JWT 토큰으로 유저 PK 가져오기
 public String getUserNum() throws BaseException{
     //1. JWT 추출
     String accessToken = getJwt();
     if(accessToken == null || accessToken.length() == 0){
         throw new BaseException(ErrorCode.COMMON_INVALID_TOKEN);
     }
     
     // 2. token 만료일자 확인
     

     // 2. JWT parsing
     Jws<Claims> claims;
     try{
         claims = Jwts.parser()
                 .setSigningKey(authKey)
                 .parseClaimsJws(accessToken);
     } catch (Exception ignored) {
         throw new BaseException(ErrorCode.COMMON_INVALID_TOKEN);
     }

     // 3. userNum 추출
     return claims.getBody().getSubject();
 }

 // JWT 토큰에서 인증 정보 조회
 public Authentication getAuthentication(String token) {
     UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
     return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
 }

 // 토큰에서 회원 정보 추출
 public String getUserPk(String token) {
     return Jwts.parser().setSigningKey(authKey).parseClaimsJws(token).getBody().getSubject();
 }
 
 // accessToken or refreshToken Claims 가져오기
 public Claims getClaim(String token) {
	 try {
		 return Jwts.parser().setSigningKey(authKey).parseClaimsJws(token).getBody();
	 } catch (SignatureException signatureException) {
		 try {
			 return Jwts.parser().setSigningKey(refreshKey).parseClaimsJws(token).getBody();
		 } catch (Exception exception) {
			 throw new BaseException(ErrorCode.COMMON_INVALID_TOKEN);
		 }
	 } catch (Exception e) {
		 throw new BaseException(ErrorCode.COMMON_INVALID_TOKEN);
		 
	 }
 }

 // 토큰의 유효성 + 만료일자 확인
 public boolean validateToken(Claims claim) {
     try {
         return !claim.getExpiration().before(new Date());
     } catch (Exception e) {
         return false;
     }
 }
}
