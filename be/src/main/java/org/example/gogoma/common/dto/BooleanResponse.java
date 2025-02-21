package org.example.gogoma.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BooleanResponse {

    private boolean isSuccess;

    public static BooleanResponse success() {
        return BooleanResponse.builder()
                .isSuccess(true)
                .build();
    }
}
