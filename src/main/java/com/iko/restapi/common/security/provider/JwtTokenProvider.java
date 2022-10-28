package com.iko.restapi.common.security.provider;

import java.util.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import com.iko.restapi.common.exception.InvalidTokenException;
import com.iko.restapi.common.security.PrincipalDetails;
import com.iko.restapi.common.security.logout.LogoutTokenService;
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
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//토큰을 생성하고 검증하는 클래스입니다.
//해당 컴포넌트는 필터클래스에서 사전 검증을 거칩니다.
@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenProvider {
    private String authKey = "ikoSecretAuthKey";
    private final String ACCESS_TOKEN_NAME = "accessToken";
    private final String REFRESH_TOKEN_NAME = "refreshToken";
    private final LogoutTokenService logoutTokenService;

    // 토큰 유효시간
    private final long authValidTime = 30 * 60 * 1000L; //30분
    private final long refreshValidDay = 14 * 24 * 60 * 60 * 1000L; // 14일

    private final UserDetailsService userDetailsService;

    // 객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        authKey = Base64.getEncoder().encodeToString(authKey.getBytes());
    }

    // JWT 토큰 생성
    public String createAccToken(String userPk, Collection<? extends GrantedAuthority> roles) {
        Claims claims = Jwts.claims().setSubject(userPk); // JWT payload 에 저장되는 정보단위, 보통 여기서 user를 식별하는 값을 넣는다.
        claims.put("roles", roles.stream().findFirst()); // 정보는 key / value 쌍으로 저장된다.
        claims.put("type", ACCESS_TOKEN_NAME);
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
        claims.put("type", REFRESH_TOKEN_NAME);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshValidDay))
                .signWith(SignatureAlgorithm.HS256, authKey)
                .compact();
    }

    // JWT 토큰 가져오기
    public String getJwt() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String authHeader = request.getHeader("Authorization");
        return authHeader == null ? null : authHeader.substring(7);
    }

    // JWT 토큰으로 유저 PK 가져오기
    public String getUserNum() throws BaseException {
        //1. JWT 추출
        String accessToken = getJwt();
        if (accessToken == null || accessToken.length() == 0) {
            throw new BaseException(ErrorCode.COMMON_INVALID_TOKEN);
        }

        // 2. token 만료일자 확인


        // 2. JWT parsing
        Jws<Claims> claims;
        try {
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
        return getAuthentication(this.getClaim(token));
    }

    public Authentication getAuthentication(Claims claims) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // accessToken or refreshToken Claims 가져오기
    public Claims getClaim(String token) {
        try {
            return Jwts.parser().setSigningKey(authKey).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            log.info("Exception" + e);
            throw new BaseException(ErrorCode.COMMON_INVALID_TOKEN, e);
        }
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(Claims claim, boolean forRefresh) {
        try {
            boolean result = claim.getExpiration().after(new Date());
            result = forRefresh
                    ? result && claim.get("type").equals(REFRESH_TOKEN_NAME)
                    : result && claim.get("type").equals(ACCESS_TOKEN_NAME);
            return result;
        } catch (Exception e) {
            return false;
        }
    }

    // 토큰 재발급
    public Map<String, String> reissueToken(String token) throws BaseException {
        Claims claim = getClaim(token);
        if (!validateToken(claim, true) || logoutTokenService.getLogoutData(token) != null) {
            throw new InvalidTokenException();
        }
        Authentication	authentication = getAuthentication(claim);
        var principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String accessToken = createAccToken(principalDetails.getUsername(), principalDetails.getAuthorities());
        String refreshToken = createRefreshToken(principalDetails.getUsername(), principalDetails.getAuthorities());
        log.info("refreshToken Request from: "+ principalDetails.getUsername());
        logoutTokenService.updateLogoutData(token, claim.getExpiration()); // logout 처리
        Map<String, String> body = new HashMap<>();
        body.put("accessToken", accessToken);
        body.put("refreshToken", refreshToken);
        return body;
    }
}
