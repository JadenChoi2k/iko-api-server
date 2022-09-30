package com.iko.restapi.controller;

import com.iko.restapi.common.exception.BaseException;
import com.iko.restapi.common.response.CommonResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorControllerAdvice {

    @ExceptionHandler(BaseException.class)
    public CommonResponse baseException(BaseException e) {
        return CommonResponse.fail(e);
    }
}

