package com.iko.restapi.controller;

import com.iko.restapi.common.exception.BaseException;
import com.iko.restapi.common.exception.EntityNotFoundException;
import com.iko.restapi.common.exception.InvalidParameterException;
import com.iko.restapi.common.exception.SystemException;
import com.iko.restapi.common.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
// 컨트롤러 어드바이스는 익셉션 핸들러를 한데 모은다. 
@RestControllerAdvice
public class ErrorControllerAdvice {

    @ExceptionHandler({BaseException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public CommonResponse baseException(BaseException e) {
        log.error("baseException[{}][{}]: {}", e.getErrorCode().getStatus(),e.getErrorCode().getDescription(), e.getMessage());
        return CommonResponse.fail(e);
    }
    
    // 해당 익셉션이 발생하면 여기로 와서 호출한다. 2개이상도 등록이 가능하다. 
    @ExceptionHandler({EntityNotFoundException.class})
    // http 응답 status값을 설정한다. 
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public CommonResponse notFoundException(EntityNotFoundException e) {
        log.error("notFoundException[{}][{}]: {}", e.getErrorCode().getStatus(),e.getErrorCode().getDescription(), e.getMessage());
        return CommonResponse.fail(e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public CommonResponse validationError(MethodArgumentNotValidException e) {
        log.error("validationError[{}][{}]: ", e.getClass(), e.getMessage(), e);
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
        log.error("runtimeExeption[{}][{}]: ", e.getClass(), e.getMessage(), e);
        return CommonResponse.fail(e);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public CommonResponse requestBodyException(Exception e) {
        log.error("requestBodyException[{}][{}]", e.getClass(), e.getMessage(), e);
        return CommonResponse.fail(new InvalidParameterException("잘못된 형식입니다"));
    }
}

