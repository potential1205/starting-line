package org.example.gogoma.exception;

import lombok.Getter;

@Getter
public class DbException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    public DbException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }

    public DbException(String message, ExceptionCode exceptionCode) {
        super(message);
        this.exceptionCode = exceptionCode;
    }
}
