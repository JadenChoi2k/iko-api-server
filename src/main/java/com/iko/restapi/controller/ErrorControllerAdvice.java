package com.iko.restapi.controller;

import com.iko.restapi.common.exception.BaseException;
import com.iko.restapi.common.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ErrorControllerAdvice {

    @ExceptionHandler({BaseException.class})
    public CommonResponse baseException(BaseException e) {
        log.error("{}: {}", e.getErrorCode(), e.getMessage());
        return CommonResponse.fail(e);
    }
}

