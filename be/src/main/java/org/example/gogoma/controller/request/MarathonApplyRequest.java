package org.example.gogoma.controller.request;

import lombok.*;
import org.example.gogoma.domain.marathon.dto.UserApplyMarathonDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarathonApplyRequest {
    private UserApplyMarathonDto userApplyMarathonDto;
    private String marathonApplyUrl;
    private int formNumber;
}
