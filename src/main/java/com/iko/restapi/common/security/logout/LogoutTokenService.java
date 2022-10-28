package com.iko.restapi.common.security.logout;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class LogoutTokenService {
    @Cacheable(cacheNames = "logoutCache", key = "#token")
    public LogoutToken getLogoutData(String token) {
        // 캐시에 없는 경우 null 반환
        return null;
    }

    @CachePut(cacheNames = "logoutCache", key = "#token")
    public LogoutToken updateLogoutData(String token, Date expireAt) {
        var logoutToken = new LogoutToken();
        logoutToken.setToken(token);
        logoutToken.setExpireAt(expireAt);
        return logoutToken;
    }

    @CacheEvict(cacheNames = "logoutCache", key = "#token")
    public boolean expireCacheData(String token) {
        log.info("expire from logout cache (token={})", token);
        return true;
    }
}
