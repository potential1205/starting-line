package org.example.gogoma.domain.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Message {

    private String token;

    private Notification notification;

}
