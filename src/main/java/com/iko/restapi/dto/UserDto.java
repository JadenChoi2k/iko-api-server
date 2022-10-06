package com.iko.restapi.dto;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

import com.iko.restapi.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
public class UserDto {

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class UserRqDto {
		private String userId;
		private String userNm;
		private String userEmail;
		private String userPhone;
		private String pw;
		private String birthday;
	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	public static class UserRsDto {
		private String userId;
		private String userNm;
		private String userEmail;
		private String userPhone;
		private String birthday;
		private String joinDt;
		private String pwUpdateDt;
		
		UserRsDto(User user) {
			this.userId = user.getUserId();
			this.userNm = user.getUserNm();
			this.userEmail = user.getUserEmail();
			this.userPhone = user.getUserPhone();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			this.birthday = sdf.format(user.getBirthday());
			this.pwUpdateDt = sdf.format(user.getPwUpdateDt());
			this.joinDt = user.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		}
		
		public static UserRsDto entityToDto(User user) {
			return new UserDto.UserRsDto(user);
		}
	}
}
