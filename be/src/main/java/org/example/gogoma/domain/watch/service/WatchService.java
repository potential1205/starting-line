package org.example.gogoma.domain.watch.service;

import org.example.gogoma.controller.request.MarathonEndInitDataRequest;
import org.example.gogoma.controller.response.MarathonStartInitDataResponse;
import org.example.gogoma.domain.watch.dto.MarathonReadyDto;

public interface WatchService {
    MarathonStartInitDataResponse sendMarathonStartInitData(String accessToken, int marathonId);

    MarathonReadyDto send(int userId, int marathonId);

    void updateMarathonEndData(String accessToken, int marathonId, MarathonEndInitDataRequest marathonEndInitDataRequest);
}
