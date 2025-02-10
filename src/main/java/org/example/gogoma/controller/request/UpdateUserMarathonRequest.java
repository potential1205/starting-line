package org.example.gogoma.controller.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserMarathonRequest {

    private int targetPace;
}
