package com.iko.restapi.common.utils;

import com.iko.restapi.common.exception.EntityNotFoundException;
import com.iko.restapi.domain.user.User;
import com.iko.restapi.repository.user.UserJpaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    // returns current user authentication
    public static Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static User getCurrentUser(UserJpaRepository userRepository) throws EntityNotFoundException {
        var authentication =  getCurrentAuthentication();
        return userRepository.findByLoginId(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다"));
    }
}
