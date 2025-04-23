package com.project.deokhugam.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 공통 예외
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.", "예상치 못한 에러입니다. 관리자에게 문의하세요."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.", "입력값을 확인해주세요."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "리소스를 찾을 수 없습니다.", "요청한 데이터를 찾을 수 없습니다."),
    ;

    private final HttpStatus status;
    private final String message;
    private final String detail;
}
