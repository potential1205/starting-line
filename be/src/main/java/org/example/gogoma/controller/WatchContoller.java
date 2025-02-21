package org.example.gogoma.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.gogoma.common.dto.BooleanResponse;
import org.example.gogoma.controller.request.MarathonEndInitDataRequest;
import org.example.gogoma.controller.response.MarathonStartInitDataResponse;
import org.example.gogoma.domain.watch.dto.MarathonReadyDto;
import org.example.gogoma.domain.watch.service.WatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/watch")
public class WatchContoller {

    private final WatchService watchService;

    @GetMapping("/start/marathons/{marathonId}")
    @Operation(summary = "마라톤 시작 데이터 전송", description = "마라톤 시작 시 필요한 데이터를 전송합니다")
    public ResponseEntity<MarathonStartInitDataResponse> sendMarathonStartInitData(
            @RequestHeader("Authorization") String accessToken, @PathVariable int marathonId) {

        MarathonStartInitDataResponse marathonStartInitDataResponse =
                watchService.sendMarathonStartInitData(accessToken, marathonId);

        return ResponseEntity.ok(marathonStartInitDataResponse);
    }

    @GetMapping("/start/marathons/{marathonId}/users/{userId}")
    @Operation(summary = "마라톤 준비 데이터 전송", description = "마라톤 준비 시 필요한 데이터를 전송합니다.")
    public ResponseEntity<MarathonReadyDto> send(
            @PathVariable int userId, @PathVariable int marathonId) {

        MarathonReadyDto marathonReadyDto =
                watchService.send(userId, marathonId);

        return ResponseEntity.ok(marathonReadyDto);
    }

    @PostMapping("/end/marathons/{marathonId}")
    @Operation(summary = "마라톤 종료 데이터 추가", description = "마라톤 종료 시 전송받은 통계 데이터를 업데이트하여 저장합니다.")
    public ResponseEntity<BooleanResponse> updateMarathonEndData(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable int marathonId,
            @RequestBody MarathonEndInitDataRequest request) {

        watchService.updateMarathonEndData(accessToken, marathonId, request);
        return ResponseEntity.ok(BooleanResponse.success());
    }
}
