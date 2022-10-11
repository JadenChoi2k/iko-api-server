package com.iko.restapi.common.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class SessionUtils {
    public static final String SESSION_NAME = "userId";

    public static Long getUserId(HttpServletRequest request) {
        var session = request.getSession();
        var sessionNames = session.getAttributeNames();
        while (sessionNames.hasMoreElements()) {
            var name = sessionNames.nextElement();
            log.info("[session] {}: {}", name, session.getAttribute(name));
        }
        Object userId = request.getSession().getAttribute(SESSION_NAME);
        return (Long) userId;
    }
}
