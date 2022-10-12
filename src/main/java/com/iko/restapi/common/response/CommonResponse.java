package com.iko.restapi.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.iko.restapi.common.exception.BaseException;
import com.iko.restapi.common.exception.ErrorCode;
import com.iko.restapi.common.exception.InvalidParameterException;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CommonResponse<T> {
    private int code;
    private String errorCode;
    private String msg;
    private T data;

    protected CommonResponse(int code, String errorCode, String msg, T data) {
        this.code = code;
        this.errorCode = errorCode;
        this.msg = msg;
        this.data = data;
    }

    public static <T> CommonResponse<T> ok() {
        return new CommonResponse(200, null, "ok", null);
    }

    public static <T> CommonResponse<T> ok(String msg) {
        return new CommonResponse(200, null, msg, null);
    }

    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse(200, null, "ok", data);
    }

    public static <T> CommonResponse<T> fail(BaseException e) {
        return new CommonResponse(e.getErrorCode().getStatus(), e.getErrorCode().name(), e.getMessage(), null);
    }

    public static <T> CommonResponse<T> fail(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        return new CommonResponse(
                HttpStatus.BAD_REQUEST.value(),
                ErrorCode.COMMON_INVALID_PARAMETER.name(),
                "field validation error",
                bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.toList())
        );
    }
    
    public static <T> CommonResponse<T> fail(Exception e) {
        return new CommonResponse(500, null, "internal server error", null);
    }
}
