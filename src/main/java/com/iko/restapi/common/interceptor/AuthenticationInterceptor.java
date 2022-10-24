package com.iko.restapi.common.interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iko.restapi.common.exception.BaseException;
import com.iko.restapi.common.security.provider.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper; //자바 객체를 json으로 serialization

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) throws Exception {
//        boolean check=checkAnnotation(handler, NoAuth.class);
//        if(check) return true;

        try{
            request.setAttribute("userNum",jwtTokenProvider.getUserNum());
            log.info("login: "+jwtTokenProvider.getUserNum());
        }catch(BaseException exception){
        	log.info("login failed: ");
        	// 토큰값으로 로그인 실패시 로그인페이지로 유도
            String requestURI= request.getRequestURI();
            Map<String,String> map=new HashMap<>();
            map.put("requestURI","/app/accounts/auth?redirectURI="+requestURI);
            String json=objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
            response.getWriter().write(json);
            return false;
        }
        return true;
    }

    private boolean checkAnnotation(Object handler,Class cls){
        HandlerMethod handlerMethod=(HandlerMethod) handler;
        if(handlerMethod.getMethodAnnotation(cls)!=null){ //해당 어노테이션이 존재하면 true.
            return true;
        }
        return false;
    }
}