package org.example.gogoma.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.gogoma.common.dto.BooleanResponse;
import org.example.gogoma.controller.request.CreateUserMarathonRequest;
import org.example.gogoma.controller.request.UpdateUserMarathonRequest;
import org.example.gogoma.controller.response.UserMarathonDetailResponse;
import org.example.gogoma.controller.response.UserMarathonSearchResponse;
import org.example.gogoma.domain.usermarathon.service.UserMarathonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/usermarathons")
public class UserMarathonController {

    private final UserMarathonService userMarathonService;

    @GetMapping
    @Operation(summary = "유저 마라톤 목록 조회", description = "해당 유저의 모든 마라톤 기록 목록을 조회합니다.")
    public ResponseEntity<UserMarathonSearchResponse> searchUserMarathonList(
            @RequestHeader("Authorization") String accessToken) {

        UserMarathonSearchResponse userMarathonSearchResponse =
                userMarathonService.searchUserMarathonList(accessToken);

        return ResponseEntity.ok(userMarathonSearchResponse);
    }

    @GetMapping("/{id}")
    @Operation(summary = "유저 마라톤 상세 조회", description = "해당 ID에 해당하는 유저 마라톤 상세 정보를 조회합니다.")
    public ResponseEntity<UserMarathonDetailResponse> getUserMarathonById(
            @RequestHeader("Authorization") String accessToken, @PathVariable int id) {

        UserMarathonDetailResponse userMarathonDetailResponse =
                userMarathonService.getUserMarathonById(accessToken, id);

        return ResponseEntity.ok(userMarathonDetailResponse);
    }

    @PatchMapping("/{marathonId}")
    @Operation(summary = "유저 마라톤 업데이트", description = "마라톤 참가 중 유저의 목표 페이스를 수정합니다.")
    public ResponseEntity<BooleanResponse> updateUserMarathon(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable int marathonId,
            @RequestBody UpdateUserMarathonRequest updateUserMarathonRequest) {

        userMarathonService.updateUserMarathon(accessToken, marathonId, updateUserMarathonRequest.getTargetPace());

        return ResponseEntity.ok(BooleanResponse.success());
    }

    @GetMapping("/duplicate/{marathonId}")
    @Operation(summary = "유저 마라톤 중복 확인", description = "해당 마라톤에 유저가 이미 등록되어 있는지 중복 여부를 확인합니다.")
    public ResponseEntity<BooleanResponse> checkDuplicateUserMarathon(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable int marathonId) {

        userMarathonService.checkDuplicateUserMarathon(accessToken, marathonId);

        return ResponseEntity.ok(BooleanResponse.success());
    }

    @PostMapping
    @Operation(summary = "유저 마라톤 등록", description = "새로운 마라톤 참여 정보를 등록합니다.")
    public ResponseEntity<BooleanResponse> createUserMarathon(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody CreateUserMarathonRequest createUserMarathonRequest) {

        userMarathonService.createUserMarathon(accessToken, createUserMarathonRequest);

        return ResponseEntity.ok(BooleanResponse.success());
    }
}
