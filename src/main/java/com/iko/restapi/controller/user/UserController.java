package com.iko.restapi.controller.user;

import com.iko.restapi.common.config.SwaggerConfig;
import com.iko.restapi.common.response.CommonResponse;
import com.iko.restapi.dto.user.UserDto;
import com.iko.restapi.dto.user.UserDto.Detail;
import com.iko.restapi.dto.user.UserDto.JoinRequest;
import com.iko.restapi.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = SwaggerConfig.USER_TAG, produces = "application/json")
@Slf4j
@RequestMapping("/api/v1/user")
@RestController
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;


	@Operation(summary = "이메일 중복 체크", description = "이메일 중복 여부를 확인한다")
	@GetMapping(value = "/exists/email")
	public CommonResponse<UserDto.Exists> existsEmail(@RequestParam String email) {
		return CommonResponse.success(
				UserDto.Exists.builder()
						.attribute("email")
						.exists(userService.existsEmail(email))
						.build()
		);
	}

	@Operation(summary = "아이디 중복 체크", description = "로그인 아이디 중복 여부를 확인한다")
	@GetMapping(value = "/exists/loginId")
	public CommonResponse<UserDto.Exists> existsLoginId(@RequestParam String loginId) {
		return CommonResponse.success(
				UserDto.Exists.builder()
						.attribute("loginId")
						.exists(userService.existsLoginId(loginId))
						.build()
		);
	}

	@Operation(summary = "회원가입", description = "회원가입 요청을 보낸다")
	@PostMapping(value = "/join")
	public CommonResponse<UserDto.Detail> UserJoin(@Validated @RequestBody JoinRequest rqDto) {
		var result = userService.join(rqDto);
		return CommonResponse.success(result);
	}

	@Operation(summary = "내 정보 확인", description = "내 정보를 GET 요청한다")
	@GetMapping("/me")
	public CommonResponse<UserDto.Detail> me() {
		return CommonResponse.success(userService.me());
	}

	@Operation(summary = "내 정보 수정", description = "내 정보를 수정한다")
	@PatchMapping("/me")
	public CommonResponse<Detail> editUser(@RequestBody UserDto.EditRequest rqDto) {
		var rsDto = userService.editUser(rqDto);
		return CommonResponse.success(rsDto);
	}

	@Operation(summary = "비밀번호 수정", description = "내 비밀번호를 수정한다")
	@PatchMapping("/me/password")
	public CommonResponse<String> updatePassword(@RequestBody UserDto.EditPasswordRequest rqDto) {
		userService.updatePassword(rqDto.getPassword());
		return CommonResponse.ok();
	}

	@Operation(summary = "유저 정보 확인", description = "유저 정보를 가져온다")
	@GetMapping("/{userId}")
	public CommonResponse<UserDto.Info> userInfo(@Parameter(description = "유저 아이디", example = "1") @PathVariable Long userId) {
		return CommonResponse.success(userService.userInfo(userId));
	}
}
