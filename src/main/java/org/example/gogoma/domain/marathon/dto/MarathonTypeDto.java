package org.example.gogoma.domain.marathon.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarathonTypeDto {
    private String courseType;
    private String price;
    private String etc;
}