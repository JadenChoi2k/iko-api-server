package com.iko.restapi.common.exception;

public class AlreadyExistException extends BaseException {
    private static final ErrorCode code = ErrorCode.COMMON_ALREADY_EXIST;

    public AlreadyExistException() {
        super(code);
    }

    public AlreadyExistException(String message) {
        super(message, code);
    }

    public AlreadyExistException(Throwable cause) {
        super(code, cause);
    }
}
