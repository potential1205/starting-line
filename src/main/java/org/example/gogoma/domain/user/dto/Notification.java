package org.example.gogoma.domain.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Notification {

    private String title;

    private String body;

}
