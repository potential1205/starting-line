package org.example.gogoma.exception;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    public ValidationException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }

    public ValidationException(String message, ExceptionCode exceptionCode) {
        super(message);
        this.exceptionCode = exceptionCode;
    }
}
