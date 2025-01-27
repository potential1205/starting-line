package org.example.gogoma.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    // Controller에서 검증시 발생할 수 있는 예외 작성
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "사용자 입력 값이 검증에 실패했습니다.", 1001),

    // Service에서 비즈니스 로직 처리시 발생할 수 있는 예외 작성
    BUSINESS_ERROR(HttpStatus.CONFLICT, "비즈니스 로직에서 예외가 발생했습니다.", 2001),

    // Repository에서 데이터베이스 조작시 발생할 수 있는 예외 작성
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 조작 과정에서 예외가 발생했습니다.", 3001),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 사용자가 존재하지 않습니다.", 3002),

    // 외부 API 사용시 발생할 수 있는 예외 작성
    EXTERNAL_API_ERROR(HttpStatus.BAD_GATEWAY, "외부 API를 호출하는 과정에서 예외가 발생했습니다.", 4001);

    private final String message;
    private final int code;
    private final HttpStatus httpStatus;

    ExceptionCode(HttpStatus httpStatus, String message, int code) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.code = code;
    }
}
