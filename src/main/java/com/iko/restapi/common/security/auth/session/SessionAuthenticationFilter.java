package com.iko.restapi.common.security.auth.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iko.restapi.common.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Enumeration;
import java.util.stream.Collectors;

import static com.iko.restapi.dto.user.UserDto.*;

@Slf4j
@RequiredArgsConstructor
public class SessionAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper om = new ObjectMapper();
        try {
            LoginRequest loginRequest = om.readValue(request.getInputStream(), LoginRequest.class);
            log.info("try to log in : {}", loginRequest.getLoginId());
            Authentication authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getLoginId(), loginRequest.getPassword());
            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
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
        response.setStatus(HttpStatus.OK.value());
        log.info("log in success: {}", principalDetails.getUsername());
        request.getSession().setAttribute("userId", principalDetails.getUser().getId());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("log in failed: {}", failed.getMessage());
        super.unsuccessfulAuthentication(request, response, failed);
    }
}
