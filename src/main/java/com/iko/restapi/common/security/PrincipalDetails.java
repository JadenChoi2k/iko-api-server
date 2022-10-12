package com.iko.restapi.common.security;

import com.iko.restapi.domain.user.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;


@Getter
public class PrincipalDetails implements UserDetails {
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getLoginId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.getUseYn();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getUseYn();
    }

    // 1년을 기준으로 합니다
    @Override
    public boolean isCredentialsNonExpired() {
        return !user.getPasswordUpdatedAt().isBefore(LocalDate.now().minusYears(1));
    }

    @Override
    public boolean isEnabled() {
        return user.getUseYn();
    }

    PrincipalDetails(User user) {
        this.user = user;
    }

    public static PrincipalDetails from(User user) {
        return new PrincipalDetails(user);
    }
}
