package com.iko.restapi.repository.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iko.restapi.domain.user.User;

public interface UserJpaRepository extends JpaRepository<User, Long> {
	Optional<User> findByLoginId(String loginId);

	boolean existsByEmail(String email);

	boolean existsByLoginId(String id);
}
