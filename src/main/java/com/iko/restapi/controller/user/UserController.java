package com.iko.restapi.controller.user;

import com.iko.restapi.dto.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.iko.restapi.common.response.CommonResponse;
import com.iko.restapi.dto.user.UserDto.JoinRequest;
import com.iko.restapi.dto.user.UserDto.Detail;
import com.iko.restapi.service.user.UserService;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequestMapping("/api/v1/user")
@RestController
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	
	@GetMapping(value = "/exists/email")
	public CommonResponse<UserDto.Exists> existsEmail(@RequestParam String email) {
		return CommonResponse.success(
				UserDto.Exists.builder()
						.attribute("email")
						.result(userService.existsEmail(email))
						.build()
		);
	}
	
	@GetMapping(value = "/exists/loginId")
	public CommonResponse<UserDto.Exists> existsLoginId(@RequestParam String loginId) {
		return CommonResponse.success(
				UserDto.Exists.builder()
						.attribute("loginId")
						.result(userService.existsLoginId(loginId))
						.build()
		);
	}
	
	@PostMapping(value = "/join")
	public CommonResponse<UserDto.Detail> UserJoin(@Validated @RequestBody JoinRequest rqDto) {
		var result = userService.join(rqDto);
		return CommonResponse.success(result);
	}

	@GetMapping("/me")
	public CommonResponse<UserDto.Detail> me() {
		return CommonResponse.success(userService.me());
	}

	@PatchMapping("/me")
	public CommonResponse<Detail> editUser(@RequestBody UserDto.EditRequest rqDto) {
		var rsDto = userService.editUser(rqDto);
		return CommonResponse.success(rsDto);
	}

	@PatchMapping("/me/password")
	public CommonResponse<String> updatePassword(@RequestBody UserDto.EditPasswordRequest rqDto) {
		userService.updatePassword(rqDto.getPassword());
		return CommonResponse.ok();
	}

	@GetMapping("/{userId}")
	public CommonResponse<UserDto.Info> userInfo(@PathVariable Long userId) {
		return CommonResponse.success(userService.userInfo(userId));
	}
}
