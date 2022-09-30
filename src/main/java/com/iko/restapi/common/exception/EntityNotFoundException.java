package com.iko.restapi.common.exception;

public class EntityNotFoundException extends BaseException {
    private static final ErrorCode code = ErrorCode.COMMON_ENTITY_NOT_FOUND;

    public EntityNotFoundException() {
        super(code);
    }

    public EntityNotFoundException(String message) {
        super(message, code);
    }

    public EntityNotFoundException(Throwable cause) {
        super(code, cause);
    }

}
