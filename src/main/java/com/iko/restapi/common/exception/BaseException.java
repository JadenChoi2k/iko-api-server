package com.iko.restapi.common.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private ErrorCode errorCode;

    public BaseException() {
        super();
    }

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }

    public BaseException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BaseException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getDescription(), cause);
        this.errorCode = errorCode;
    }
}
