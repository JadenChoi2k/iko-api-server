package com.iko.restapi.common.security;

import com.iko.restapi.repository.User.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    // user_repository
    private final UserJpaRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        log.info("loadUserByUsername({})", loginId);
        return userRepository.findByLoginId(loginId)
                .map(PrincipalDetails::from)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("user_login_id %s not found", loginId)));
    }
}
