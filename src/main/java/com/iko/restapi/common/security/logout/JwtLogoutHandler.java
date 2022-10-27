package com.iko.restapi.common.security.logout;

import com.iko.restapi.common.exception.BaseException;
import com.iko.restapi.common.security.provider.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {
    private final LogoutTokenService logoutTokenService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            token = token.substring(7);
            try {
                Claims claim = jwtTokenProvider.getClaim(token);
                LocalDateTime expirationDateTime = claim.getExpiration().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
                logoutTokenService.updateLogoutData(token, expirationDateTime);
            } catch (BaseException e) {
                try {
                    response.sendError(400, "invalid token");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }
}
