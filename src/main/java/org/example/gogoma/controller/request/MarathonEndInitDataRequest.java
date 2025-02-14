package org.example.gogoma.controller.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarathonEndInitDataRequest {
    private int currentPace;
    private int runningTime;
    private int totalMemberCount;
    private int myRank;
}
