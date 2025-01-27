package org.example.gogoma.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.gogoma.common.dto.ExcpetionResponse;
import org.example.gogoma.exception.type.BusinessException;
import org.example.gogoma.exception.type.DbException;
import org.example.gogoma.exception.type.ExternalApiException;
import org.example.gogoma.exception.type.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ExcpetionResponse> handleValidationException(ValidationException ex) {

        return ResponseEntity
                .status(ex.getExceptionCode().getHttpStatus())
                .body(ExcpetionResponse.of(ex.getExceptionCode()));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ExcpetionResponse> handleBusinessException(BusinessException ex) {

        return ResponseEntity
                .status(ex.getExceptionCode().getHttpStatus())
                .body(ExcpetionResponse.of(ex.getExceptionCode()));
    }

    @ExceptionHandler(DbException.class)
    public ResponseEntity<ExcpetionResponse> handleDbException(DbException ex) {

        return ResponseEntity
                .status(ex.getExceptionCode().getHttpStatus())
                .body(ExcpetionResponse.of(ex.getExceptionCode()));
    }

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<ExcpetionResponse> handleExternalApiException(ExternalApiException ex) {

        return ResponseEntity
                .status(ex.getExceptionCode().getHttpStatus())
                .body(ExcpetionResponse.of(ex.getExceptionCode()));
    }
}