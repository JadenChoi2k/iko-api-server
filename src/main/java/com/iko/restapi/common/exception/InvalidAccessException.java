package com.iko.restapi.common.exception;

public class InvalidAccessException extends BaseException {
    private static final ErrorCode code = ErrorCode.COMMON_INVALID_ACCESS;

    public InvalidAccessException() {
        super(code);
    }

    public InvalidAccessException(String message) {
        super(message, code);
    }

    public InvalidAccessException(Throwable cause) {
        super(code, cause);
    }
}
