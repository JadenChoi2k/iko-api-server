package com.iko.restapi.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    /**
     * COMMON-ERROR: 공통적으로 처리되는 오류 목록
     */
    // 500 에러로, 집중 모니터링 대상
    COMMON_SYSTEM_ERROR("일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요."), // 500
    COMMON_INVALID_PARAMETER("요청한 값이 올바르지 않습니다."), // 400
    COMMON_ENTITY_NOT_FOUND("존재하지 않는 엔티티입니다."), // 404
    COMMON_UNAUTHORIZED("권한이 없습니다."), // 401
    COMMON_INVALID_ACCESS("잘못된 접근입니다."), // 400
    COMMON_INVALID_TOKEN("유효하지 않은 토큰값입니다."), // 401
    COMMON_ALREADY_EXIST("이미 존재하는 객체입니다."); // 400

    private final String description;
}
