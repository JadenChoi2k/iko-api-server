package com.iko.restapi.controller.user;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iko.restapi.common.security.PrincipalDetailsService;
import com.iko.restapi.common.security.provider.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {

	private final PrincipalDetailsService principalDetailsService;
	private final JwtTokenProvider jwtTokenProvider;
	@PostMapping("/login")
    public String login(@RequestBody Map<String, String> user) {
		UserDetails member = principalDetailsService.loadUserByUsername(user.get("loginId").toString());
		return jwtTokenProvider.createToken(member.getUsername(), member.getAuthorities());
	}
}
