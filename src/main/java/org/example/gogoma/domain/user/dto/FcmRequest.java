package org.example.gogoma.domain.user.dto;

import lombok.Data;

@Data
public class FcmRequest {

    private int userId;
    private String username;
    private int marathonId;
    private String marathonName;
}
