package com.iko.restapi.controller.user;

import com.iko.restapi.common.response.CommonResponse;
import com.iko.restapi.common.security.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value="/auth")
public class TokenController {
	private final JwtTokenProvider jwtTokenProvider;

	@PostMapping("/refresh")
	public CommonResponse<Map<String, String>> refreshToken(@RequestHeader(value="Authorization") String refreshToken){
		// 토큰담는 형식 주의: RequestHeader.Authorization: Bearer $token
		String token = refreshToken.substring(7);
		return CommonResponse.success(jwtTokenProvider.reissueToken(token));
	}
}
