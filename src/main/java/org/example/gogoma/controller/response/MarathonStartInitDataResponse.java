package org.example.gogoma.controller.response;

import lombok.Builder;
import lombok.Getter;
import org.example.gogoma.domain.user.entity.Friend;

import java.util.List;

@Getter
@Builder
public class MarathonStartInitDataResponse {

    private int userId;
    private int marathonId;
    private String userName;
    private int targetPace;
    private int runningDistance;
    private String marathonTitle;
    private List<Friend> friendList;

    public static MarathonStartInitDataResponse of(
            int userId, String userName, int targetPace, int marathonId, String marathonTitle, List<Friend> friendList) {

        return MarathonStartInitDataResponse.builder()
                .userId(userId)
                .userName(userName)
                .targetPace(targetPace)
                .marathonId(marathonId)
                .marathonTitle(marathonTitle)
                .friendList(friendList)
                .build();
    }
}
