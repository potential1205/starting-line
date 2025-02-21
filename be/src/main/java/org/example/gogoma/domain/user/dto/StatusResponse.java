package org.example.gogoma.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusResponse {

    private String status;

    public static StatusResponse of(String status) {
        return StatusResponse.builder()
                .status(status)
                .build();
    }
}
