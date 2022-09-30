package com.iko.restapi.common.exception;

public class InvalidParameterException extends BaseException {
    private static final ErrorCode code = ErrorCode.COMMON_INVALID_PARAMETER;

    public InvalidParameterException() {
        super(code);
    }

    public InvalidParameterException(String message) {
        super(message, code);
    }

    public InvalidParameterException(Throwable cause) {
        super(code, cause);
    }
}
