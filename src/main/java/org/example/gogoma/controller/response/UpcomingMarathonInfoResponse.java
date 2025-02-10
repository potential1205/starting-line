package org.example.gogoma.controller.response;

import lombok.Builder;
import lombok.Getter;
import org.example.gogoma.domain.marathon.entity.Marathon;

@Getter
@Builder
public class UpcomingMarathonInfoResponse {

    private Marathon marathon;

    public static UpcomingMarathonInfoResponse of(Marathon marathon) {
        return UpcomingMarathonInfoResponse.builder()
                .marathon(marathon)
                .build();
    }

}
