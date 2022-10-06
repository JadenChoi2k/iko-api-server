package com.iko.restapi.controller.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iko.restapi.common.exception.BaseException;
import com.iko.restapi.common.response.CommonResponse;
import com.iko.restapi.dto.UserDto;
import com.iko.restapi.dto.UserDto.UserRqDto;
import com.iko.restapi.dto.UserDto.UserRsDto;
import com.iko.restapi.service.User.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/v1/user")
@RestController	
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ObjectMapper mapper;
	
	@RequestMapping(value = "/emailExists", method=RequestMethod.GET)
	public CommonResponse<String> emailCheck(@RequestParam String email) {
		log.info("Id Exist Check Start. email: " + email);
		try {
			boolean result = false;
			result = userService.emailCheck(email);
			log.info("email Exist Check Result : " + result);
			if(result) {
				return CommonResponse.success("email "+email+" exist");
			} else {
				return CommonResponse.success("email "+email+" not exist");
			}
		} catch (Exception e) {
			log.error("email Exist Check Error. " + e);
			return CommonResponse.fail(e);
		}
	}
	
	@RequestMapping(value = "/idExists", method=RequestMethod.GET)
	public CommonResponse<String> idCheck(@RequestParam String id) {
		log.info("Id Exist Check Start. Id: " + id);
		try {
			boolean result = false;
			result = userService.idCheck(id);
			log.info("Id Exist Check Result : " + result);
			if(result) {
				return CommonResponse.success("id "+id+" exist");
			} else {
				return CommonResponse.success("id "+id+" not exist");
			}
		} catch (Exception e) {
			log.error("Id Exist Check Error. " + e);
			return CommonResponse.fail(e);
		}
	}
	
	@RequestMapping(value = "/join", method=RequestMethod.POST)
	public CommonResponse<String> UserJoin(@RequestBody UserRqDto rqDto) throws JsonProcessingException{
		log.info("Join Start. JoinRqDto: " + mapper.writeValueAsString(rqDto));
		String result = new String();		
		try {
			result = userService.userJoin(rqDto);
			log.info("userJoin Seccess. rqDto id"+rqDto.getUserId());
			return CommonResponse.success(result);
		} catch (BaseException baseException) {
			log.info("UserJoin Failed. rqDto id: " + rqDto.getUserId());
			return CommonResponse.ok(baseException.getMessage());
		} catch (Exception e) {
			log.error("UserJoin Error. " + e);
			return CommonResponse.fail(e);
		}
	}
	
	@RequestMapping(value = "/login", method=RequestMethod.POST) 
	public CommonResponse<UserRsDto> Login(@RequestBody UserRqDto rqDto) throws JsonProcessingException {
		log.info("Login Start. rqDto id: " + rqDto.getUserId());
		UserRsDto rsDto = new UserRsDto();
		try {
			rsDto = userService.login(rqDto);
			log.info("Login Success. UserRsDto: "+mapper.writeValueAsString(rsDto));
			return CommonResponse.success(rsDto);
		} catch (BaseException baseException) {
			log.info("Login Failed. rqDto userId: " + rqDto.getUserId());
			return CommonResponse.ok(baseException.getMessage());
		} catch (Exception e) {
			log.error("Login Failed. " + e);
			return CommonResponse.fail(e);
		}
	}
	
	@RequestMapping(value = "/edit", method=RequestMethod.POST)
	public CommonResponse<UserRsDto> editUser(@RequestBody UserRqDto rqDto) {
		log.info("editUser Start. rqDto id: " + rqDto.getUserId());
		UserRsDto rsDto = new UserRsDto();
		try {
			rsDto = userService.editUser(rqDto);
			log.info("EditUser Success. UserRsDto: "+ mapper.writeValueAsString(rsDto));
			return CommonResponse.success(rsDto);
		} catch (BaseException baseException) {
			log.info("EditUser Failed. rqDto userId: " + rqDto.getUserId());
			return CommonResponse.ok(baseException.getMessage());
		} catch (Exception e) {
			log.error("EditUser Failed. "+ e);
			return CommonResponse.fail(e);
		}
	}
	
	@RequestMapping(value = "/pwEdit", method=RequestMethod.POST)
	public CommonResponse<String> pwUpdate(@RequestBody UserRqDto rqDto) {
		log.info("pwUpdate Start. rqDto id: " + rqDto.getUserId());
		String rsDto = new String();
		try {
			rsDto = userService.pwUpdate(rqDto);
			log.info("pwUpdate Success. rqDto Id: "+ rqDto.getUserId());
			return CommonResponse.success(rsDto);
		} catch (BaseException baseException) {
			log.info("pwUpdate Failed. rqDto userId: " + rqDto.getUserId());
			return CommonResponse.ok(baseException.getMessage());
		} catch (Exception e) {
			log.error("pwUpdate Failed. "+ e);
			return CommonResponse.fail(e);
		}
	}
}
