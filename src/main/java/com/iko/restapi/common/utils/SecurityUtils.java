package com.iko.restapi.common.utils;

import com.iko.restapi.common.exception.BaseException;
import com.iko.restapi.common.exception.EntityNotFoundException;
import com.iko.restapi.common.exception.InvalidAccessException;
import com.iko.restapi.domain.user.User;
import com.iko.restapi.repository.user.UserJpaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    // returns current user authentication
    public static Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static User getCurrentUser(UserJpaRepository userRepository) throws BaseException {
        var authentication =  getCurrentAuthentication();
        if (authentication == null) {
            throw new InvalidAccessException("접근 권한이 없습니다");
        }
        return userRepository.findByLoginId(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다"));
    }
}
