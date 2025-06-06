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
    private int marathonType;
    private String dDay;
    private LocalDateTime raceStartDateTime;
    private LocalDateTime paymentDateTime;

    public static UserMarathonSearchDto of(int userMarathonId,
                                           String marathonTitle, int marathonType, String dDay,
            LocalDateTime raceStartDateTime, LocalDateTime paymentDateTime) {

        return UserMarathonSearchDto.builder()
                .userMarathonId(userMarathonId)
                .marathonTitle(marathonTitle)
                .marathonType(marathonType)
                .dDay(dDay)
                .raceStartDateTime(raceStartDateTime)
                .paymentDateTime(paymentDateTime)
                .build();
    }
}
