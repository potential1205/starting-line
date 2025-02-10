package org.example.gogoma.domain.watch.service;

import org.example.gogoma.controller.response.MarathonStartInitDataResponse;

public interface WatchService {
    MarathonStartInitDataResponse sendMarathonStartInitData(String accessToken, int marathonId);
}
