package com.iko.restapi.common.exception;

public class SystemException extends BaseException {
    private static final ErrorCode code = ErrorCode.COMMON_SYSTEM_ERROR;

    public SystemException() {
        super(code);
    }

    public SystemException(String message) {
        super(message, code);
    }

    public SystemException(Throwable cause) {
        super(code, cause);
    }
}
