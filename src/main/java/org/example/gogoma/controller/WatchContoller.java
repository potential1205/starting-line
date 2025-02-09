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

    @GetMapping("/ranking")
    public ResponseEntity<List<String>> getRanking() {
        List<String> rankingList = Arrays.asList(
                "",
                "1st 김지수",
                "2nd 김용현",
                "3rd 박민경",
                "4th 백지민",
                "5th 이재훈",
                "6th 이주호",
                ""
        );

        System.out.println("Send a message to the watch");

        return ResponseEntity.ok(rankingList);
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendString(@RequestBody String request) {
        System.out.println("Receive a message from the watch " + request);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/start/{userId}")
    public ResponseEntity<String> sendMarathonStartInitData(@PathVariable int userId) {

        watchService.sendMarathonStartInitData(userId);

        return ResponseEntity.ok().build();
    }
}
