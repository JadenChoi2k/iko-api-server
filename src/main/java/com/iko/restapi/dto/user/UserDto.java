package com.iko.restapi.dto.user;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.iko.restapi.domain.user.User;

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
		@Size(min=4, max=12, message="set Id between 6~12 long")
		private String loginId;
		@NotBlank
		private String username;
		@Email(message = "invalid email type")
		private String email;
		// 폰넘버 타입 정의하기
		@NotBlank
		@Pattern(regexp="^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$", message="invalid phone number")
		private String phone;
		// 비밀번호 검증 (사용자 PW - (영문, 특수문자, 숫자 포함 8자 이상 ~ 20자))
		@NotBlank
		@Pattern(regexp="^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$", message="invalid password")
		private String password;
		// 날짜 여기서 검증하기
		@NotBlank
//		@Pattern(regexp="^((19|20)\\\\d\\\\d)?([- /.])?(0[1-9]|1[012])([- /.])?(0[1-9]|[12][0-9]|3[01])$")
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
