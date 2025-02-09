package org.example.gogoma.controller.response;

import lombok.Builder;
import lombok.Getter;
import org.example.gogoma.domain.watch.dto.FriendInfoDto;

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
    private List<FriendInfoDto> friendInfoDtoList;
}
