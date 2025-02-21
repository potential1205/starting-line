package org.example.gogoma.domain.watch.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.gogoma.domain.user.entity.Friend;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class MarathonReadyDto {

    private int userId;
    private int marathonId;
    private String userName;
    private int targetPace;
    private int runningDistance;
    private String marathonTitle;
    private LocalDateTime marathonStartTime;
    private List<Friend> friendList;

    public static MarathonReadyDto of(
            int userId, String userName, int targetPace, int marathonId, String marathonTitle, List<Friend> friendList, LocalDateTime marathonStartTime) {

        return MarathonReadyDto.builder()
                .userId(userId)
                .userName(userName)
                .targetPace(targetPace)
                .marathonId(marathonId)
                .marathonTitle(marathonTitle)
                .friendList(friendList)
                .marathonStartTime(marathonStartTime)
                .build();
    }
}