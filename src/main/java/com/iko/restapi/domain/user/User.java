package com.iko.restapi.domain.user;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.iko.restapi.common.entity.BaseTimeEntity;
import com.iko.restapi.common.exception.BaseException;
import com.iko.restapi.common.exception.ErrorCode;
import com.iko.restapi.dto.UserDto;
import com.iko.restapi.dto.UserDto.UserRqDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "usr")
public class User extends BaseTimeEntity {

	@Id
	@GeneratedValue
	@Column(name = "usr_sno")
	private Long usrSno;
   
   @Column(name = "usr_id", unique = true)
   private String userId;
   
   @Column(name = "usr_name")
   private String userNm;
   
   @Column(name = "usr_email")
   private String userEmail;
   
   @Column(name = "usr_phone")
   private String userPhone;
   
   @Column(name = "pswd")
   private String pw;
   
   @Column(name = "brth_dt")
   private Date birthday;
   
   // 계정의 사용여부
   @Column(name = "use_yn")
   private String useYn;

   @Column(name = "pw_updt_dt")
   private Date pwUpdateDt;

//	 Order, Cart 나중에 생성
//   @OneToMany(mappedBy = "user", orphanRemoval = false )
//   private List<Order> orders = new ArrayList<>();   
//   
//   @OneToMany(mappedBy = "user", orphanRemoval = false )
//   private List<Cart> carts = new ArrayList<>();   
   
   User(UserRqDto rqDto){
	   this.userId = rqDto.getUserId();
	   this.userNm = rqDto.getUserNm();
	   this.userEmail = rqDto.getUserEmail(); 
	   this.userPhone = rqDto.getUserPhone();
   }
   
   public static User dtoToEntity(UserRqDto rqDto) throws Exception {
		User userJoinEntity = new User(rqDto);
		
		userJoinEntity.pw = User.SHA512(rqDto.getPw());
		
		// front 에서 검증하길..
		SimpleDateFormat sdfBirthday = new SimpleDateFormat("yyyy-MM-dd");
		userJoinEntity.birthday = sdfBirthday.parse(rqDto.getBirthday());	
		
		Date today = new Date();
		userJoinEntity.pwUpdateDt = today;
		userJoinEntity.useYn = "Y";
		
		// 이메일 형식 검증
		Pattern p = Pattern.compile("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$");
		Matcher m = p.matcher(rqDto.getUserEmail());
		if(m.matches()) {
			userJoinEntity.userEmail = rqDto.getUserEmail();
		} else {
			throw new BaseException("Wrong Email Address", ErrorCode.COMMON_INVALID_PARAMETER);
		}
		return userJoinEntity;
	}   
   
   public static String SHA512(String password) {
		String salt = "aDielfksnelk34lksdf"+password;
		String hex = null;
		try {
			MessageDigest msg = MessageDigest.getInstance("SHA-512");
			msg.update(salt.getBytes());
			
			hex = String.format("%128x", new BigInteger(1, msg.digest()));
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hex;
	}
   
   public void update(UserRqDto rqDto) {
	   if(null != rqDto.getUserNm()) {
		   this.userNm = rqDto.getUserNm();
	   }
	   if(null != rqDto.getUserPhone()) {
		   this.userPhone = rqDto.getUserPhone();
	   }
	   if(null != rqDto.getUserEmail()) {
		   this.userEmail = rqDto.getUserEmail();
	   }
   }
   
   public void pwUpdate(UserRqDto rqDto) {
	   if(null != rqDto.getPw()) {
		   this.pw = SHA512(rqDto.getPw());
		   this.pwUpdateDt = new Date();
	   } else {
		   throw new BaseException("비밀번호없음", ErrorCode.COMMON_INVALID_PARAMETER);
	   }
   }
   
}