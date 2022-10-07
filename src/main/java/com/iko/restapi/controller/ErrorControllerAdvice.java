package com.iko.restapi.controller;

import com.iko.restapi.common.exception.BaseException;
import com.iko.restapi.common.exception.SystemException;
import com.iko.restapi.common.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ErrorControllerAdvice {

    @ExceptionHandler({BaseException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public CommonResponse baseException(BaseException e) {
        log.error("[{}][{}]: {}", e.getErrorCode().getStatus(),e.getErrorCode().getDescription(), e.getMessage());
        return CommonResponse.fail(e);
    }

    /**
     * 런타임 오류나 시스템 에러 등 중대한 오류, 집중 모니터링 대상
     * @param e
     * @return
     */
    @ExceptionHandler({RuntimeException.class, SystemException.class})
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResponse runtimeException(RuntimeException e) {
        log.error("[{}][{}]: ", e.getClass(), e.getMessage(), e);
        return CommonResponse.fail(e);
    }
}

