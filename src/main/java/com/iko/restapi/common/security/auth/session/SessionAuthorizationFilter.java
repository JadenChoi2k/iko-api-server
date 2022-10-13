package com.iko.restapi.common.security.auth.session;

import com.iko.restapi.common.exception.EntityNotFoundException;
import com.iko.restapi.common.security.PrincipalDetails;
import com.iko.restapi.common.utils.SessionUtils;
import com.iko.restapi.domain.user.User;
import com.iko.restapi.repository.user.UserJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class SessionAuthorizationFilter extends BasicAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final UserJpaRepository userRepository;

    public SessionAuthorizationFilter(AuthenticationManager authenticationManager, UserJpaRepository userRepository) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        Long userId = SessionUtils.getUserId(request);
        if (userId == null) {
            chain.doFilter(request, response);
            return;
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저입니다."));
        var principalDetails = PrincipalDetails.from(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }
}
