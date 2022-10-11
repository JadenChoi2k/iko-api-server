package com.iko.restapi.controller.User;

import com.iko.restapi.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.iko.restapi.common.response.CommonResponse;
import com.iko.restapi.dto.UserDto.JoinRequest;
import com.iko.restapi.dto.UserDto.Detail;
import com.iko.restapi.service.User.UserService;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequestMapping("/api/v1/user")
@RestController
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	
	@GetMapping(value = "/exists/email")
	public CommonResponse<UserDto.Exists> emailCheck(@RequestParam String email) {
		log.info("email exist check: " + email);
		return CommonResponse.success(
				UserDto.Exists.builder()
						.attribute("email")
						.result(userService.emailCheck(email))
						.build()
		);
	}
	
	@GetMapping(value = "/exists/loginId")
	public CommonResponse<UserDto.Exists> loginIdCheck(@RequestParam String loginId) {
		log.info("loginId exist check: " + loginId);
		return CommonResponse.success(
				UserDto.Exists.builder()
						.attribute("loginId")
						.result(userService.loginIdCheck(loginId))
						.build()
		);
	}
	
	@PostMapping(value = "/join")
	public CommonResponse<UserDto.Detail> UserJoin(@Validated @RequestBody JoinRequest rqDto) {
		log.info("Join Start. JoinRqDto: " + rqDto.toString());
		var result = userService.userJoin(rqDto);
		log.info("userJoin Seccess. loginId: {}", rqDto.getLoginId());
		return CommonResponse.success(result);
	}
	
	@PostMapping(value = "/edit")
	public CommonResponse<Detail> editUser(@RequestBody UserDto.EditRequest rqDto) {
		log.info("editUser Start. rqDto id: [[userId]]"); // TODO: implement
		var rsDto = userService.editUser(rqDto);
		log.info("EditUser Success. UserRsDto: {}", rsDto.toString());
		return CommonResponse.success(rsDto);
	}
	
	@PostMapping(value = "/pwEdit")
	public CommonResponse<String> pwUpdate(@RequestBody UserDto.EditPwRequest rqDto) {
		log.info("pwUpdate Start. rqDto id: "); // todo: session 조회
		userService.pwUpdate(rqDto.getPassword());
		log.info("pwUpdate Success. rqDto Id: ");
		return CommonResponse.success("success");
	}

	@GetMapping("/me")
	public CommonResponse<UserDto.Detail> me() {
		return CommonResponse.success(userService.me());
	}
}
