package com.iko.restapi.common.exception;

public class InvalidTokenException extends BaseException {
    private static final ErrorCode code = ErrorCode.COMMON_INVALID_TOKEN;

    public InvalidTokenException() {
        super(code);
    }

    public InvalidTokenException(String message) {
        super(message, code);
    }

    public InvalidTokenException(Throwable cause) {
        super(code, cause);
    }
}
