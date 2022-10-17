package com.iko.restapi.domain.user;

import com.iko.restapi.common.exception.InvalidParameterException;
import com.iko.restapi.dto.user.UserDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

public class UserTest {

    public static User createUser() {
        return User.of(
            new UserDto.JoinRequest(
                    "login",
                    "username",
                    "M",
                    "email@email.com",
                    "010-1234-5678",
                    "password",
                    "2000-01-01"
            )
        );
    }

    @Test
    void create() {
        var user = User.of(
                new UserDto.JoinRequest(
                        "login",
                        "username",
                        "M",
                        "email@email.com",
                        "010-1234-5678",
                        "password",
                        "2000-01-01"
                )
        );

        assertThat(user.getUseYn()).isTrue();
        assertThat(user.getLoginId()).isEqualTo("login");
        assertThat(user.getEmail()).isEqualTo("email@email.com");
        assertThat(user.getGender()).isEqualTo(User.Gender.M);
        assertThat(user.getPhone()).isEqualTo("010-1234-5678");
        assertThat(user.getBirthday()).isEqualTo(LocalDate.of(2000, 1, 1));
        assertThat(user.getPasswordUpdatedAt()).isNotNull();
    }

    @Test
    void updateProfile() {
        // given
        String updateName = "updatedName";
        String updatePhone = null;
        String updateEmail = "update@email.com";
        User user = createUser();
        // when
        user.updateProfile(updateName, updatePhone, updateEmail);
        // then
        assertThat(user.getUsername()).isEqualTo(updateName);
        assertThat(user.getPhone()).isNotEqualTo(updatePhone);
        assertThat(user.getEmail()).isEqualTo(updateEmail);
    }

    @Test
    void updatePassword() {
        // given
        String updatedPassword = "updatePassword";
        User user = createUser();
        // when
        user.updatePassword(updatedPassword);
        // then
        assertThat(user.getPassword()).isEqualTo(updatedPassword);
    }

    @Test
    void updatePasswordInvalidParameterException() {
        // given
        String updatedPassword = null;
        User user = createUser();
        // when, then
        assertThatThrownBy(() -> user.updatePassword(updatedPassword))
                .isInstanceOf(InvalidParameterException.class);
    }
}
