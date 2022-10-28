package com.iko.restapi.controller.user;

import com.iko.restapi.common.exception.InvalidParameterException;
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
		if (refreshToken == null || refreshToken.length() < 7) {
			throw new InvalidParameterException("Authorization 헤더를 입력해주세요");
		}
		String token = refreshToken.substring(7);
		return CommonResponse.success(jwtTokenProvider.reissueToken(token));
	}
}
