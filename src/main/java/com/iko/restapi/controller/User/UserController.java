package com.iko.restapi.controller.User;

import com.iko.restapi.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iko.restapi.common.exception.BaseException;
import com.iko.restapi.common.response.CommonResponse;
import com.iko.restapi.dto.UserDto.JoinRequest;
import com.iko.restapi.dto.UserDto.Detail;
import com.iko.restapi.service.User.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/v1/user")
@RestController
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	
	private final ObjectMapper mapper;
	
	@GetMapping(value = "/exists/email")
	public CommonResponse<UserDto.Exists> emailCheck(@RequestParam String email) {
		log.info("email exist check: " + email);
		return CommonResponse.success(
				UserDto.Exists.builder()
						.attributeName("email")
						.result(userService.emailCheck(email))
						.build()
		);
	}
	
	@GetMapping(value = "/exists/loginId")
	public CommonResponse<UserDto.Exists> loginIdCheck(@RequestParam String loginId) {
		log.info("loginId exist check: " + loginId);
		return CommonResponse.success(
				UserDto.Exists.builder()
						.attributeName("loginId")
						.result(userService.loginIdCheck(loginId))
						.build()
		);
	}
	
	@PostMapping(value = "/join")
	public CommonResponse<UserDto.Detail> UserJoin(@Validated @RequestBody JoinRequest rqDto) throws JsonProcessingException{
		log.info("Join Start. JoinRqDto: " + mapper.writeValueAsString(rqDto));
		try {
			var result = userService.userJoin(rqDto);
			log.info("userJoin Seccess. loginId: {}", rqDto.getLoginId());
			return CommonResponse.success(result);
		} catch (BaseException baseException) {
			log.info("UserJoin Failed. loginId: {}", rqDto.getLoginId());
			return CommonResponse.ok(baseException.getMessage());
		} catch (Exception e) {
			log.error("UserJoin Error. " + e);
			return CommonResponse.fail(e);
		}
	}
	
	@PostMapping(value = "/login")
	public CommonResponse<Detail> Login(@RequestBody UserDto.LoginRequest loginRequest) throws JsonProcessingException {
		log.info("Login Start. rqDto id: " + loginRequest.getLoginId());
		Detail rsDto = new Detail();
		try {
			rsDto = userService.login(loginRequest.getLoginId(), loginRequest.getPassword());
			log.info("Login Success. UserRsDto: "+mapper.writeValueAsString(rsDto));
			return CommonResponse.success(rsDto);
		} catch (BaseException baseException) {
			log.info("Login Failed. rqDto userId: " + loginRequest.getLoginId());
			return CommonResponse.ok(baseException.getMessage());
		} catch (Exception e) {
			log.error("Login Failed. " + e);
			return CommonResponse.fail(e);
		}
	}
	
	@PostMapping(value = "/edit")
	public CommonResponse<Detail> editUser(@RequestBody UserDto.EditRequest rqDto) {
		log.info("editUser Start. rqDto id: [[userId]]"); // TODO: implement
		Detail rsDto = new Detail();
		try {
			rsDto = userService.editUser(rqDto);
			log.info("EditUser Success. UserRsDto: "+ mapper.writeValueAsString(rsDto));
			return CommonResponse.success(rsDto);
		} catch (BaseException baseException) {
			log.info("EditUser Failed. rqDto userId: [[userId]]");
			return CommonResponse.ok(baseException.getMessage());
		} catch (Exception e) {
			log.error("EditUser Failed. "+ e);
			return CommonResponse.fail(e);
		}
	}
	
	@PostMapping(value = "/pwEdit")
	public CommonResponse<String> pwUpdate(@RequestBody UserDto.EditPwRequest rqDto) {
//		log.info("pwUpdate Start. rqDto id: " + rqDto.getLoginId());
		String rsDto = new String();
		try {
			rsDto = userService.pwUpdate(rqDto.getPassword());
//			log.info("pwUpdate Success. rqDto Id: "+ rqDto.getLoginId());
			return CommonResponse.success(rsDto);
		} catch (BaseException baseException) {
//			log.info("pwUpdate Failed. rqDto userId: " + rqDto.getLoginId());
			return CommonResponse.ok(baseException.getMessage());
		} catch (Exception e) {
			log.error("pwUpdate Failed. "+ e);
			return CommonResponse.fail(e);
		}
	}
}
