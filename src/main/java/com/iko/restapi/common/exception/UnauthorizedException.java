package com.iko.restapi.common.exception;

public class UnauthorizedException extends BaseException {
    private static final ErrorCode code = ErrorCode.COMMON_UNAUTHORIZED;

    public UnauthorizedException() {
        super(code);
    }

    public UnauthorizedException(String message) {
        super(message, code);
    }

    public UnauthorizedException(Throwable cause) {
        super(code, cause);
    }

}
