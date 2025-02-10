package org.example.gogoma.domain.usermarathon.service;

import org.example.gogoma.controller.response.UpcomingMarathonInfoResponse;
import org.example.gogoma.controller.response.UserMarathonDetailResponse;
import org.example.gogoma.controller.response.UserMarathonSearchResponse;

public interface UserMarathonService {
    UserMarathonSearchResponse searchUserMarathonList(String accessToken);

    UserMarathonDetailResponse getUserMarathonById(String accessToken, int userMarathonId);

    UpcomingMarathonInfoResponse getUpcomingMarathonInfo(String accessToken, int dDay);
}
