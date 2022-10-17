package com.iko.restapi.domain.user;

import java.time.LocalDate;

import javax.persistence.*;

import com.iko.restapi.common.entity.BaseTimeEntity;
import com.iko.restapi.common.exception.InvalidParameterException;
import com.iko.restapi.common.utils.DataUtils;
import com.iko.restapi.dto.user.UserDto.JoinRequest;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "usr")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 15, nullable = false)
    private Role role = Role.ROLE_MEMBER;

    @Column(name = "login_id", unique = true, length = 30, nullable = false)
    private String loginId;

    @Column(name = "username", length = 20, nullable = false)
    private String username;

    @Column(name = "gender", length = 1)
    @Enumerated(EnumType.STRING)
    private Gender gender = Gender.X;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "pswd", nullable = false)
    private String password;

    @Column(name = "birthday")
    private LocalDate birthday;

    // 계정의 사용여부
    @Column(name = "use_yn")
    private Boolean useYn = true;

    @Column(name = "pw_updt_dt")
    private LocalDate passwordUpdatedAt;

    @Getter
    @RequiredArgsConstructor
    public enum Role {
        ROLE_MEMBER("멤버"), ROLE_SELLER("판매자"), ROLE_ADMIN("관리자");

        private final String description;
    }
    
    @Getter
    @RequiredArgsConstructor
    public enum Gender {
        M("남성"), F("여성"), X("없음");
        
        private final String description;

        static Gender from(String s) {
            if (s == null) return X;
            switch (s) {
                case "M":
                    return M;
                case "F":
                    return F;
                default:
                    return X;
            }
        }
    }

    public static User of(JoinRequest joinRequest) throws RuntimeException {
        User joinUser = new User();

        joinUser.loginId = joinRequest.getLoginId();
        joinUser.username = joinRequest.getUsername();
        joinUser.gender = Gender.from(joinRequest.getGender());
        joinUser.email = joinRequest.getEmail();
        joinUser.phone = joinRequest.getPhone();
        joinUser.password = joinRequest.getPassword();
        joinUser.birthday = DataUtils.parseBirthday(joinRequest.getBirthday());
        joinUser.passwordUpdatedAt = LocalDate.now();
        joinUser.useYn = true;

        return joinUser;
    }

    public void updateProfile(String username, String phone, String email) {
        if (username != null) {
            this.username = username;
        }
        if (phone != null) {
            this.phone = phone;
        }
        if (email != null) {
            this.email = email;
        }
    }

    /**
     * 인코딩된 패스워드만 입력되도록 한다
     * @param password
     */
    public void updatePassword(String password) {
        if (password != null) {
            this.password = password;
            this.passwordUpdatedAt = LocalDate.now();
        } else {
            throw new InvalidParameterException("비밀번호없음");
        }
    }

}