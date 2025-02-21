package org.example.gogoma.domain.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FcmRequest {

    private int userId;
    private String username;
    private int marathonId;
    private String marathonName;

    public static FcmRequest of(UserAlertInfo userAlertInfo, int marathonId, String marathonName) {
        return FcmRequest.builder()
                .userId(userAlertInfo.getId())
                .username(userAlertInfo.getName())
                .marathonId(marathonId)
                .marathonName(marathonName)
                .build();
    }
}
