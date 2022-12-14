package com.iko.restapi.service.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iko.restapi.common.exception.BaseException;
import com.iko.restapi.common.exception.EntityNotFoundException;
import com.iko.restapi.common.exception.InvalidParameterException;
import com.iko.restapi.common.security.provider.JwtTokenProvider;
import com.iko.restapi.common.utils.SecurityUtils;
import com.iko.restapi.domain.user.User;
import com.iko.restapi.domain.user.User.Role;
import com.iko.restapi.dto.user.UserDto;
import com.iko.restapi.dto.user.UserDto.Detail;
import com.iko.restapi.dto.user.UserDto.JoinRequest;
import com.iko.restapi.repository.user.UserJpaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService /*implements UserDetailsService */  {
	private final BCryptPasswordEncoder passwordEncoder;
	private final UserJpaRepository userJpaRepository;
	// 이메일 조회
	@Transactional(readOnly = true)
	public boolean existsEmail(String email) {
		boolean result = userJpaRepository.existsByEmail(email);
		log.info("email Exist Check Result : {}", result);
		return result;
	}
	
	// 아이디 조회
	@Transactional(readOnly = true)
	public boolean existsLoginId(String loginId) {
		boolean result = userJpaRepository.existsByLoginId(loginId);
		log.info("loginId Exist Check for {} : {}", loginId, result);
		return result;
	}
	
	// 회원가입
	public Detail join(JoinRequest joinRequest) throws InvalidParameterException {
		if(!existsEmail(joinRequest.getEmail()) && !existsLoginId(joinRequest.getLoginId())) {
			joinRequest.setPassword(passwordEncoder.encode(joinRequest.getPassword()));
			User user = userJpaRepository.save(User.of(joinRequest));
			log.info("user({}) joined", user.getId());
			return Detail.from(user);
		} throw new InvalidParameterException("중복된 이메일 또는 아이디입니다");
	}

	@Transactional(readOnly = true)
	public Detail me() {
		return Detail.from(
				SecurityUtils.getCurrentUser(userJpaRepository)
		);
	}
	
	// 회원정보 수정
	@Transactional
	public Detail editUser(UserDto.EditRequest editRequest) throws BaseException {
		// 요청한 유저에 대한 정보는 세션에서 가져온다.
		log.info("user edit request: {}", editRequest);
		var user = SecurityUtils.getCurrentUser(userJpaRepository);
		user.updateProfile(
				editRequest.getUsername(),
				editRequest.getPhone(),
				editRequest.getEmail()
		);
		log.info("user updated - userId: {}", user.getId());
//		userJpaRepository.save(user); // jpa 변경감지 기능 작동
		return Detail.from(user);
	}
	
	// 비밀번호 재설정
	@Transactional
	public void updatePassword(String password) throws BaseException {
		// 세션에서 조회
		var user = SecurityUtils.getCurrentUser(userJpaRepository);
		log.info("user({}) updated password", user.getId());
		user.updatePassword(passwordEncoder.encode(password));
		userJpaRepository.save(user);
	}

	// 유저 info 가져오기(
	public UserDto.Info userInfo(Long id) {
		log.info("find user info - userId={}", id);
		var userInfoById = userJpaRepository.findUserInfoById(id)
				.orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다"));
		return UserDto.Info.builder()
				.id(userInfoById.getId())
				.username(userInfoById.getUsername())
				.build();
	}

//	public String login(Map<String, String> user) {
//		// TODO Auto-generated method stub
//		log.info("user email = {}", user.get("email"));
//        User member = userJpaRepository.findByLoginId(user.get("loginId"))
//                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
//        List<String> roleList = new ArrayList<String>();
//        roleList.add(member.getRole().toString());
//        return jwtTokenProvider.createToken(member.getUsername(), roleList);
//	}
//
//	
//	 @Override 
//	 public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException { // TODO Auto-generated method stub return
//		 User member = userJpaRepository.findByLoginId(userId).orElseThrow( () -> new
//				 EntityNotFoundException("유저를 찾을 수 없습니다")); 
//		 return member;
//	 }
	 
}
