package org.example.gogoma.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.gogoma.exception.ExceptionCode;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcpetionResponse {

    private int code;
    private String message;

    public static ExcpetionResponse of(ExceptionCode exceptionCode) {
        return ExcpetionResponse.builder()
                .code(exceptionCode.getCode())
                .message(exceptionCode.getMessage())
                .build();
    }
}
