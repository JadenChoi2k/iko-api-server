package com.iko.restapi.service.User;

import javax.transaction.Transactional;

import com.iko.restapi.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.iko.restapi.common.exception.BaseException;
import com.iko.restapi.common.exception.ErrorCode;
import com.iko.restapi.domain.user.User;
import com.iko.restapi.dto.UserDto.JoinRequest;
import com.iko.restapi.dto.UserDto.Detail;
import com.iko.restapi.repository.User.UserJpaRepository;

@Slf4j
@Service
public class UserService {
	@Autowired
	private UserJpaRepository userJpaRepository;
	
	// 이메일 조회
	public boolean emailCheck(String email) {
		boolean result = userJpaRepository.existsByEmail(email);
		log.info("email Exist Check Result : " + result);
		return result;
	}
	
	// 아이디 조회
	public boolean idCheck(String id) {
		return userJpaRepository.existsByLoginId(id);
	}
	
	// 회원가입
	public String userJoin(JoinRequest rqDto) throws Exception {
		if(!emailCheck(rqDto.getEmail()) && !idCheck(rqDto.getLoginId())) {
			userJpaRepository.save(User.dtoToEntity(rqDto));
			return "가입성공";
		} throw new BaseException("이메일, 아이디 중복으로 가입실패", ErrorCode.COMMON_INVALID_PARAMETER);
	}
	
	// 로그인
	public Detail login(String loginId, String password) throws Exception {
		User user = userJpaRepository.findByLoginId(loginId).orElseThrow(
				()-> new BaseException("아이디없음", ErrorCode.COMMON_INVALID_PARAMETER));
		if(null != user) {			
			String pw = user.getPassword();
			String enpw = User.SHA512(password);
			if(!pw.equals(enpw)) {
				throw new BaseException("비밀번호 틀림", ErrorCode.COMMON_INVALID_PARAMETER);
			}
		}
		if(!"Y".equals(user.getUseYn())) {
			throw new BaseException("사용 안하는 계정", ErrorCode.COMMON_INVALID_ACCESS);
		} else {
			return Detail.entityToDto(user);
		}
	}
	
	// 회원정보 수정
	@Transactional
	public Detail editUser(UserDto.EditRequest rqDto) throws Exception {
		// 요청한 유저에 대한 정보는 세션에서 가져온다.
		Long userId = -1L; // TODO: implement
		User user = userJpaRepository.findById(userId).orElseThrow(
				()-> new BaseException("아이디없음", ErrorCode.COMMON_INVALID_PARAMETER));
		user.update(rqDto);
		userJpaRepository.save(user);
		return Detail.entityToDto(user);
	}
	
	// 비밀번호 재설정
	@Transactional
	public String pwUpdate(String newPw) throws Exception {
		// 세션에서 조회
		Long userId = -1L; // todo: implement
		User user = userJpaRepository.findById(userId).orElseThrow(
				()-> new BaseException("아이디없음", ErrorCode.COMMON_INVALID_PARAMETER));
		user.pwUpdate(newPw);
		userJpaRepository.save(user);
		return "reset";
	}
}
