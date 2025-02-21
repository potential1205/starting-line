package org.example.gogoma.controller.response;

import lombok.Builder;
import lombok.Getter;
import org.example.gogoma.domain.marathon.entity.Marathon;

@Getter
@Builder
public class UpcomingMarathonInfoResponse {

    private Marathon marathon;
    private boolean isEnd;

    public static UpcomingMarathonInfoResponse of(Marathon marathon, boolean isEnd) {
        return UpcomingMarathonInfoResponse.builder()
                .marathon(marathon)
                .isEnd(isEnd)
                .build();
    }

}
