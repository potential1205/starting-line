package org.example.gogoma.exception.type;

import lombok.Getter;
import org.example.gogoma.exception.ExceptionCode;

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
