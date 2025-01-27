package org.example.gogoma.exception.type;

import lombok.Getter;
import org.example.gogoma.exception.ExceptionCode;

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
