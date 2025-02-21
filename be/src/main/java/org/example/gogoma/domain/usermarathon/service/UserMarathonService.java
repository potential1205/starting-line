package org.example.gogoma.domain.usermarathon.service;

import org.example.gogoma.controller.request.CreateUserMarathonRequest;
import org.example.gogoma.controller.response.UserMarathonDetailResponse;
import org.example.gogoma.controller.response.UserMarathonSearchResponse;

public interface UserMarathonService {
    UserMarathonSearchResponse searchUserMarathonList(String accessToken);

    UserMarathonDetailResponse getUserMarathonById(String accessToken, int userMarathonId);

    void updateUserMarathon(String accessToken, int marathonId, int targetPace);

    void checkDuplicateUserMarathon(String accessToken, int marathonId);

    void createUserMarathon(String accessToken, CreateUserMarathonRequest createUserMarathonRequest);
}
