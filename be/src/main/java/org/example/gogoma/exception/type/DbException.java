package org.example.gogoma.exception.type;

import lombok.Getter;
import org.example.gogoma.exception.ExceptionCode;

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
