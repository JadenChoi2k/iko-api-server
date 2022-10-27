package com.iko.restapi.controller.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iko.restapi.common.exception.BaseException;
import com.iko.restapi.common.exception.ErrorCode;
import com.iko.restapi.common.response.CommonResponse;
import com.iko.restapi.common.security.PrincipalDetails;
import com.iko.restapi.common.security.provider.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value="/auth")
public class TokenController {
	private final JwtTokenProvider jwtTokenProvider;

	@RequestMapping(value="/refresh", method=RequestMethod.POST)
	public CommonResponse<Map<String, String>> refreshToken(@RequestHeader(value="Authorization") String refreshToken){
		// 토큰담는 형식 주의: RequestHeader.Authorization: Bearer $token
		try {
			String token = refreshToken.substring(7);
			Authentication	authentication = jwtTokenProvider.getAuthentication(token);
			var principalDetails = (PrincipalDetails) authentication.getPrincipal();
			String renewedAccessToken = jwtTokenProvider.createAccToken(principalDetails.getUsername(), principalDetails.getAuthorities());
			log.info("refreshToken Request from: "+ principalDetails.getUsername());
			Map<String, String> newTokenMap = new HashMap<String, String>();
			newTokenMap.put("accessToken", renewedAccessToken);
			return CommonResponse.success(newTokenMap);
		} catch (Exception e) {
			throw new BaseException(ErrorCode.COMMON_INVALID_TOKEN, e);
		}
	}
}
