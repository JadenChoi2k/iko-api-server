package com.iko.restapi.controller.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iko.restapi.common.response.CommonResponse;
import com.iko.restapi.dto.UserDto;
import com.iko.restapi.dto.UserDto.Detail;
import com.iko.restapi.dto.UserDto.JoinRequest;
import com.iko.restapi.service.User.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
	public CommonResponse<UserDto.Detail> UserJoin(@Validated @RequestBody JoinRequest rqDto) {
		log.info("Join Start. JoinRqDto: " + rqDto.toString());
		var result = userService.userJoin(rqDto);
		log.info("userJoin Seccess. loginId: {}", rqDto.getLoginId());
		return CommonResponse.success(result);
	}
	
	@PostMapping(value = "/login")
	public CommonResponse<Detail> Login(@RequestBody UserDto.LoginRequest loginRequest, HttpServletRequest request) {
		log.info("Login Start. rqDto id: {}", loginRequest.getLoginId());
		Detail rsDto = userService.login(loginRequest.getLoginId(), loginRequest.getPassword());
		// session 저장
		HttpSession session = request.getSession();
		if(session.isNew()) {
			request.getSession().setAttribute("loginId", rsDto.getLoginId());
		}
		log.info("Login Success. loginId: {}", rsDto.getLoginId());
		return CommonResponse.success(rsDto);
	}
	
	@PostMapping(value = "/edit")
	public CommonResponse<Detail> editUser(@RequestBody UserDto.EditRequest rqDto) {
		log.info("editUser Start. rqDto id: [[userId]]"); 
		var rsDto = userService.editUser(rqDto);
		log.info("EditUser Success. UserRsDto: {}", rsDto.toString());
		return CommonResponse.success(rsDto);
	}
	
	@PostMapping(value = "/pwEdit")
	public CommonResponse<String> pwUpdate(@RequestBody UserDto.EditPwRequest rqDto, HttpServletRequest request) {
		log.info("pwUpdate Start. rqDto id: "); 
		userService.pwUpdate(rqDto.getPassword());
		log.info("pwUpdate Success. rqDto Id: ");
		return CommonResponse.success("success");
	}
//	
//	@GetMapping(value = "userInfo")
//	public CommonResponse<String> userInfo(@RequestBody UserDto.Info rqDto, HttpServletRequest request) {
//		User user = 
//		return CommonResponse.success(userService.userInfo(rqDto));
//	}
}
