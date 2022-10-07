package com.iko.restapi.domain.user;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
import com.iko.restapi.common.exception.InvalidParameterException;
import com.iko.restapi.common.utils.DataUtils;
import com.iko.restapi.dto.UserDto;
import com.iko.restapi.dto.UserDto.JoinRequest;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "usr")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(name = "login_id", unique = true)
    private String loginId;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "pswd")
    private String password;

    @Column(name = "birthday")
    private LocalDate birthday;

    // 계정의 사용여부
    @Column(name = "use_yn")
    private Boolean useYn;

    @Column(name = "pw_updt_dt")
    private LocalDate pwUpdateDt;

//	 Order, Cart 나중에 생성
//   @OneToMany(mappedBy = "user", orphanRemoval = false )
//   private List<Order> orders = new ArrayList<>();   
//   
//   @OneToMany(mappedBy = "user", orphanRemoval = false )
//   private List<Cart> carts = new ArrayList<>();   

    User(JoinRequest rqDto) {
        this.loginId = rqDto.getLoginId();
        this.username = rqDto.getUsername();
        this.email = rqDto.getEmail();
        this.phone = rqDto.getPhone();
    }

    public static User dtoToEntity(JoinRequest rqDto) throws RuntimeException {
        User userJoinEntity = new User(rqDto);

        userJoinEntity.password = User.SHA512(rqDto.getPassword());
        userJoinEntity.birthday = DataUtils.parseBirthday(rqDto.getBirthday());
        userJoinEntity.pwUpdateDt = LocalDate.now();
        userJoinEntity.useYn = true;
        userJoinEntity.email = rqDto.getEmail();
        // 이메일 형식 검증
        // 필드에서 @Email 통해 검증
//        Pattern p = Pattern.compile("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$");
//        Matcher m = p.matcher(rqDto.getEmail());
//        if (m.matches()) {
//            userJoinEntity.email = rqDto.getEmail();
//        } else {
//            throw new BaseException("Wrong Email Address", ErrorCode.COMMON_INVALID_PARAMETER);
//        }
        return userJoinEntity;
    }

    public static String SHA512(String password) {
        String salt = "aDielfksnelk34lksdf" + password;
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

    public void update(UserDto.EditRequest rqDto) {
        if (null != rqDto.getUsername()) {
            this.username = rqDto.getUsername();
        }
        if (null != rqDto.getPhone()) {
            this.phone = rqDto.getPhone();
        }
        if (null != rqDto.getEmail()) {
            this.email = rqDto.getEmail();
        }
    }

    public void pwUpdate(String password) {
        if (password != null) {
            this.password = SHA512(password);
            this.pwUpdateDt = LocalDate.now();
        } else {
            throw new InvalidParameterException("비밀번호없음");
        }
    }

}