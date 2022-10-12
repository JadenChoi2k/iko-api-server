package com.iko.restapi.repository.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iko.restapi.domain.user.User;
import com.iko.restapi.dto.UserInfoMapping;

public interface UserJpaRepository extends JpaRepository<User, Long> {
	Optional<User> findByLoginId(String loginId);

	boolean existsByEmail(String email);

	boolean existsByLoginId(String id);
	
	List<UserInfoMapping> findAllById(Long Id);
	
}
