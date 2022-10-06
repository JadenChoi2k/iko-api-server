package com.iko.restapi.dto;

import lombok.Data;

public class UserDto {
    @Data
    public static class LoginRequest {
        private String loginId;
        private String password;
    }
}
