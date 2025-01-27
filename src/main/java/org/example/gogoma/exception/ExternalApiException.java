package org.example.gogoma.exception;

import lombok.Getter;

@Getter
public class ExternalApiException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    public ExternalApiException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }

    public ExternalApiException(String message, ExceptionCode exceptionCode) {
        super(message);
        this.exceptionCode = exceptionCode;
    }
}
