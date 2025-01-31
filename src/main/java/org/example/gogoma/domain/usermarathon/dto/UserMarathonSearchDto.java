package org.example.gogoma.domain.usermarathon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMarathonSearchDto {
    private int userMarathonId;
    private String marathonTitle;
    private String marathonType;
    private String dday;
    private LocalDateTime raceStartDateTime;
    private LocalDateTime paymentDateTime;

    public static UserMarathonSearchDto of(int userMarathonId,
            String marathonTitle, String marathonType, String dday,
            LocalDateTime raceStartDateTime, LocalDateTime paymentDateTime) {

        return UserMarathonSearchDto.builder()
                .marathonTitle(marathonTitle)
                .marathonType(marathonType)
                .dday(dday)
                .raceStartDateTime(raceStartDateTime)
                .paymentDateTime(paymentDateTime)
                .build();
    }
}
