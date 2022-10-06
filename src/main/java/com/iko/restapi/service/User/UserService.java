package com.iko.restapi.service.User;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.iko.restapi.common.exception.BaseException;
import com.iko.restapi.common.exception.ErrorCode;
import com.iko.restapi.domain.user.User;
import com.iko.restapi.dto.UserDto;
import com.iko.restapi.dto.UserDto.UserRqDto;
import com.iko.restapi.dto.UserDto.UserRsDto;
import com.iko.restapi.repository.User.UserJpaRepository;

@Service
public class UserService {
	@Autowired
	private UserJpaRepository userJpaRepository;
	
	// 이메일 조회
	public boolean emailCheck(String email) {
		return userJpaRepository.existsByuserEmail(email);	
	}
	
	// 아이디 조회
	public boolean idCheck(String id) {
		return userJpaRepository.existsByuserId(id);	
	}
	
	// 회원가입
	public String userJoin(UserRqDto rqDto) throws Exception {
		if(!emailCheck(rqDto.getUserEmail()) && !idCheck(rqDto.getUserId())) {
			userJpaRepository.save(User.dtoToEntity(rqDto));
			return "가입성공";
		} throw new BaseException("이메일, 아이디 중복으로 가입실패", ErrorCode.COMMON_INVALID_PARAMETER);
	}
	
	// 로그인
	public UserRsDto login(UserRqDto rqDto) throws Exception {
		User user = userJpaRepository.findByuserId(rqDto.getUserId()).orElseThrow(
				()-> new BaseException("아이디없음", ErrorCode.COMMON_INVALID_PARAMETER));
		if(null != user) {			
			String pw = user.getPw();
			String enpw = User.SHA512(rqDto.getPw());
			if(!pw.equals(enpw)) {
				throw new BaseException("비밀번호 틀림", ErrorCode.COMMON_INVALID_PARAMETER);
			}
		}
		if(!"Y".equals(user.getUseYn())) {
			throw new BaseException("사용 안하는 계정", ErrorCode.COMMON_INVALID_ACCESS);
		} else {
			return UserRsDto.entityToDto(user);
		}
	}
	
	// 회원정보 수정
	@Transactional
	public UserRsDto editUser(UserRqDto rqDto) throws Exception {
		User user = userJpaRepository.findByuserId(rqDto.getUserId()).orElseThrow(
				()-> new BaseException("아이디없음", ErrorCode.COMMON_INVALID_PARAMETER));
		user.update(rqDto);
		userJpaRepository.save(user);
		return UserDto.UserRsDto.entityToDto(user);
	}
	
	// 비밀번호 재설정
	@Transactional
	public String pwUpdate(UserRqDto rqDto) throws Exception {
		User user = userJpaRepository.findByuserId(rqDto.getUserId()).orElseThrow(
				()-> new BaseException("아이디없음", ErrorCode.COMMON_INVALID_PARAMETER));
		user.pwUpdate(rqDto);
		userJpaRepository.save(user);
		return "reset";
	}
}
