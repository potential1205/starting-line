package org.example.gogoma.controller;


import lombok.RequiredArgsConstructor;
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

    @PostMapping("/start/{userId}")
    public ResponseEntity<String> sendMarathonStartInitData(@PathVariable int userId) {

        watchService.sendMarathonStartInitData(userId);

        return ResponseEntity.ok().build();
    }
}
