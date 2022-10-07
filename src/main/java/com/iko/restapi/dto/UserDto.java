package com.iko.restapi.dto;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

import com.iko.restapi.domain.user.User;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Getter
@Setter
public class UserDto {

	/* Request */

	@Data
	public static class LoginRequest {
		@NotBlank
		private String loginId;
		@NotBlank
		private String password;
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class JoinRequest {
		@NotBlank
		private String loginId;
		@NotBlank
		private String username;
		@Email(message = "invalid email type")
		private String email;
		// todo: 폰넘버 타입 정의하기
		@NotBlank
		private String phone;
		// todo: 비밀번호 검증
		@NotBlank
		private String password;
		// 날짜 여기서 검증하기
		@NotBlank
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
	public static class EditPwRequest {
		@NotBlank
		private String password;
	}

	/* Response */

	@Data
	@Builder
	public static class Exists {
		private String attributeName;
		private Boolean result;
	}

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
		private LocalDateTime joinDt;
		private String pwUpdateDt;
		
		Detail(User user) {
			this.loginId = user.getLoginId();
			this.username = user.getUsername();
			this.email = user.getEmail();
			this.phone = user.getPhone();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			this.birthday = sdf.format(user.getBirthday());
			this.joinDt = user.getCreatedAt();
			this.pwUpdateDt = sdf.format(user.getPwUpdateDt());
		}
		
		public static Detail from(User user) {
			return new Detail(user);
		}
	}

	@Data
	public static class Info {
		private Long id;
		private String username;
	}
}
