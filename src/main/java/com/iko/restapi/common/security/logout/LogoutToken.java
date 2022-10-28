package com.iko.restapi.common.security.logout;

import lombok.Data;

import java.util.Date;

@Data
public class LogoutToken {
    private String token;
    // TODO: 만료 후 캐시에서 삭제
    private Date expireAt;
}
