package com.iko.restapi.dto.user;

import java.time.format.DateTimeFormatter;

import com.iko.restapi.dto.user.validation.LoginId;
import com.iko.restapi.dto.user.validation.Password;
import com.iko.restapi.dto.user.validation.Phone;
import com.iko.restapi.dto.user.validation.Username;
import com.iko.restapi.domain.user.User;

import lombok.*;

import javax.validation.constraints.*;


@Getter
@Setter
public class UserDto {

	/* Request */

	@Data
	public static class LoginRequest {
		@NotBlank(message = "아이디를 입력해주세요")
		private String loginId;
		@NotBlank(message = "비밀번호를 입력해주세요")
		private String password;
	}

	@ToString
	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class JoinRequest {
		@LoginId
		private String loginId;
		@Username
		private String username;
		@Email(message = "적절하지 않은 이메일 형식입니다")
		private String email;
		@Phone
		private String phone;
		@Password
		private String password;
		// todo: 날짜 검증 애노테이션 작성
		@NotBlank
		private String birthday;
	}

	// todo: nullable한 validation 애노테이션 작성
	@Data
	public static class EditRequest {
		private String username;
		private String email;
		private String phone;
		private String birthday;
	}

	@Data
	public static class EditPasswordRequest {
		@NotBlank(message = "비밀번호를 입력해주세요")
		private String password;
	}

	/* Response */

	@Data
	@Builder
	public static class Exists {
		private String attribute;
		private Boolean result;
	}

	@ToString
	@Getter
	@Setter
	@NoArgsConstructor
	public static class Detail {
		private Long id;
		private String loginId;
		private String username;
		private String email;
		private String phone;
		private String birthday;
		private String joinedAt;
		private String passwordUpdatedAt;
		
		Detail(User user) {
			this.id = user.getId();
			this.loginId = user.getLoginId();
			this.username = user.getUsername();
			this.email = user.getEmail();
			this.phone = user.getPhone();
			var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			this.birthday = user.getBirthday().format(formatter);
			this.joinedAt = user.getCreatedAt().format(formatter);
			this.passwordUpdatedAt = user.getPasswordUpdatedAt().format(formatter);
		}
		
		public static Detail from(User user) {
			return new Detail(user);
		}
	}

	@Data
	public static class Info {
		private Long id;
		private String username;
		// 프로필 사진, 리뷰 개수 등 공개 가능한 정보만 필드에 작성
	}
}
