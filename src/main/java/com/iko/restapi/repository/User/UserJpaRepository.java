package com.iko.restapi.repository.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iko.restapi.domain.user.User;

public interface UserJpaRepository extends JpaRepository<User, Long> {

	boolean existsByuserEmail(String email);

	boolean existsByuserId(String id);

	Optional<User> findByuserId(String userId);
}
