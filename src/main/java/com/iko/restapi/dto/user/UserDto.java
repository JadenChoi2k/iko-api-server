package com.iko.restapi.dto.user;

import java.time.format.DateTimeFormatter;

import javax.validation.constraints.*;

import com.iko.restapi.domain.user.User;

import com.iko.restapi.dto.user.validation.annotation.DateNullable;
import com.iko.restapi.dto.user.validation.annotation.GenderNullable;
import com.iko.restapi.dto.user.validation.annotation.PhoneNullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


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
		@NotBlank
		@Pattern(regexp = "[a-zA-Z0-9]{8,30}", message = "아이디는 영어, 숫자로 이루어진 8~30자의 문자열입니다")
		private String loginId;
		@NotBlank
		@Pattern(regexp = "[a-zA-Z0-9가-힣]{2,20}", message = "유저 이름은 영어, 숫자, 한글로 이루어진 2~20자의 문자열입니다")
		private String username;
		@GenderNullable
		private String gender;
		@Email(message = "적절하지 않은 이메일 형식입니다")
		private String email;
		// 폰넘버 타입 정의하기
//		@Pattern(regexp="^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$", message="적절하지 않은 전화번호 형식입니다")
		@PhoneNullable
		private String phone;
		// 비밀번호 검증 (사용자 PW - (영문, 특수문자, 숫자 포함 8자 이상 ~ 20자))
		@Pattern(regexp="^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,30}$", message="비밀번호는 영문, 특수문자, 숫자를 포함한 8~30자의 문자열입니다")
		private String password;
		// 날짜 여기서 검증하기
		@DateNullable
		private String birthday;
	}

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
			this.birthday = user.getBirthday() != null ? user.getBirthday().format(formatter) : null;
			this.joinedAt = user.getCreatedAt().format(formatter);
			this.passwordUpdatedAt = user.getPasswordUpdatedAt().format(formatter);
		}
		
		public static Detail from(User user) {
			return new Detail(user);
		}
	}

	@Data
	@Builder
	public static class Info {
		private Long id;
		private String username;
		// 프로필 사진, 리뷰 개수 등 공개 가능한 정보만 필드에 작성
	}
}
