package com.iko.restapi.domain.user;

import java.time.LocalDate;

import javax.persistence.*;

import com.iko.restapi.common.entity.BaseTimeEntity;
import com.iko.restapi.common.exception.InvalidParameterException;
import com.iko.restapi.common.utils.DataUtils;
import com.iko.restapi.dto.user.UserDto.JoinRequest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "usr")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 15, nullable = false)
    private Role role = Role.ROLE_MEMBER;

    @Column(name = "login_id", unique = true, length = 30)
    private String loginId;

    @Column(name = "username", length = 20)
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
    private LocalDate passwordUpdatedAt;

    @Getter
    @RequiredArgsConstructor
    public enum Role {
        ROLE_MEMBER("멤버"), ROLE_SELLER("판매자"), ROLE_ADMIN("관리자");

        private final String description;
    }

    User(JoinRequest rqDto) {
        this.loginId = rqDto.getLoginId();
        this.username = rqDto.getUsername();
        this.email = rqDto.getEmail();
        this.phone = rqDto.getPhone();
    }

    public static User of(JoinRequest joinRequest) throws RuntimeException {
        User joinUser = new User(joinRequest);

        joinUser.password = joinRequest.getPassword();
        joinUser.birthday = DataUtils.parseBirthday(joinRequest.getBirthday());
        joinUser.passwordUpdatedAt = LocalDate.now();
        joinUser.useYn = true;
        joinUser.email = joinRequest.getEmail();

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

    public void updatePassword(String password) {
        if (password != null) {
            this.password = password;
            this.passwordUpdatedAt = LocalDate.now();
        } else {
            throw new InvalidParameterException("비밀번호없음");
        }
    }

}