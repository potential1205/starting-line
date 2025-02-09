package org.example.gogoma.controller;


import lombok.RequiredArgsConstructor;
import org.example.gogoma.controller.response.MarathonStartInitDataResponse;
import org.example.gogoma.domain.watch.service.WatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/watch")
public class WatchContoller {

    private final WatchService watchService;

    @GetMapping("/start/users/{userId}/marathons/{marathonId}")
    public ResponseEntity<MarathonStartInitDataResponse> sendMarathonStartInitData(@PathVariable int userId, @PathVariable int marathonId) {

        MarathonStartInitDataResponse marathonStartInitDataResponse =
                watchService.sendMarathonStartInitData(userId, marathonId);

        return ResponseEntity.ok(marathonStartInitDataResponse);
    }
}
