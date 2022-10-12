package com.iko.restapi.service.user;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.iko.restapi.common.exception.BaseException;
import com.iko.restapi.common.exception.ErrorCode;
import com.iko.restapi.common.exception.InvalidParameterException;
import com.iko.restapi.domain.user.User;
import com.iko.restapi.dto.UserDto;
import com.iko.restapi.dto.UserDto.Detail;
import com.iko.restapi.dto.UserDto.Info;
import com.iko.restapi.dto.UserDto.JoinRequest;
import com.iko.restapi.dto.UserInfoMapping;
import com.iko.restapi.repository.User.UserJpaRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
	private final UserJpaRepository userJpaRepository;
	
	// 이메일 조회
	public boolean emailCheck(String email) {
		boolean result = userJpaRepository.existsByEmail(email);
		log.info("email Exist Check Result : " + result);
		return result;
	}
	
	// 아이디 조회
	public boolean loginIdCheck(String id) {
		boolean result = userJpaRepository.existsByLoginId(id);
		log.info("loginId Exist Check Result : " + result);
		return result;
	}
	
	// 회원가입
	@Transactional
	public Detail userJoin(JoinRequest rqDto) throws InvalidParameterException {
		if(!emailCheck(rqDto.getEmail()) && !loginIdCheck(rqDto.getLoginId())) {
			User user = userJpaRepository.save(User.dtoToEntity(rqDto));
			return Detail.from(user); 
		} throw new InvalidParameterException("이메일, 아이디 중복으로 가입실패");
	}
	
	// 회원정보 수정
	@Transactional
	public Detail editUser(UserDto.EditRequest rqDto) throws BaseException {
		// 요청한 유저에 대한 정보는 세션에서 가져온다.
		ServletRequestAttributes servletRequestAttribute = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession httpSession = servletRequestAttribute.getRequest().getSession();
		String loginId = httpSession.getAttribute("loginId").toString();
		User user = userJpaRepository.findByLoginId(loginId).orElseThrow(
				()-> new BaseException("아이디없음", ErrorCode.COMMON_INVALID_PARAMETER));
		user.update(rqDto);
		userJpaRepository.save(user);
		return Detail.from(user);
	}
	
	// 비밀번호 재설정
	@Transactional
	public void pwUpdate(String newPw) throws BaseException {
		// 세션에서 조회
		ServletRequestAttributes servletRequestAttribute = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession httpSession = servletRequestAttribute.getRequest().getSession();
		String loginId = httpSession.getAttribute("loginId").toString(); 
		User user = userJpaRepository.findByLoginId(loginId).orElseThrow(
				()-> new BaseException("아이디없음", ErrorCode.COMMON_INVALID_PARAMETER));
		user.pwUpdate(newPw);
		userJpaRepository.save(user);
	}
	
	// 유저 info 가져오기(
	public UserDto.Info userInfo(Long id) {
		Info rs = new Info();
		List<UserInfoMapping> userInfoById = userJpaRepository.findAllById(id);
		if(!userInfoById.isEmpty()) {
			UserInfoMapping userInfo = userInfoById.stream().findFirst().get();
			rs.setId(userInfo.getId());
			rs.setUsername(userInfo.getUsername());
		} else {
			throw new BaseException("아이디없음", ErrorCode.COMMON_INVALID_PARAMETER);
		}
		return rs;
	}
}
