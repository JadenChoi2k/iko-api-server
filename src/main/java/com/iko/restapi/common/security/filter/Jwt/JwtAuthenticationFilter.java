package com.iko.restapi.common.security.filter.Jwt;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iko.restapi.common.exception.ErrorCode;
import com.iko.restapi.common.security.PrincipalDetails;
import com.iko.restapi.common.security.provider.JwtTokenProvider;
import com.iko.restapi.dto.user.UserDto.LoginRequest;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    	try {
        	String decoded=null;
			try {
				String idpw = request.getHeader("Authorization").substring(6);
				decoded = new String(Base64.getDecoder().decode(idpw));
			} catch (IllegalArgumentException e) {
				log.info("ERROR: " + e);
				throw new AuthenticationCredentialsNotFoundException(decoded);
			} catch (NullPointerException ne) {
				log.info("ERROR: " + ne);
				throw new AuthenticationCredentialsNotFoundException(decoded);
			}
			int idx = decoded.indexOf(":");
			String id = decoded.substring(0, idx);
			String pw = decoded.substring(idx+1);
            log.info("try to log in : {}", id);
            Authentication authenticationToken =
                    new UsernamePasswordAuthenticationToken(id, pw);
            return authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            log.error("exception while trying to log in: {}", e.getMessage());
            try {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "invalid json type");
                return null;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        var principalDetails = (PrincipalDetails) authResult.getPrincipal();
        log.info("log in success: {}", principalDetails.getUsername());
        // Create JWT Token
        String jwtAccToken = jwtTokenProvider.createAccToken(principalDetails.getUsername(), principalDetails.getAuthorities());
        String jwtRefreshToken = jwtTokenProvider.createRefreshToken(principalDetails.getUsername(), principalDetails.getAuthorities());
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", jwtAccToken);
        tokens.put("refreshToken", jwtRefreshToken);
        response.resetBuffer();
        response.setStatus(HttpStatus.OK.value());
        response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        // Set Token in body
        response.getOutputStream().print(new ObjectMapper().writeValueAsString(tokens));
        request.getSession().setAttribute("userId", principalDetails.getUser().getId());
        response.flushBuffer();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("log in failed: {}", failed.getMessage());
        super.unsuccessfulAuthentication(request, response, failed);
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    		throws IOException, ServletException {
    	super.doFilter(request, response, chain);
    }
}
