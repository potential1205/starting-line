package org.example.gogoma.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.gogoma.common.dto.BooleanResponse;
import org.example.gogoma.controller.request.CreateMarathonRequest;
import org.example.gogoma.domain.marathon.service.MarathonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/marathons")
public class MarathonController {

    private final MarathonService marathonService;

    /**
     * 마라톤 정보를 저장하는 API
     * @param createMarathonRequest 마라톤 정보를 담은 요청 객체
     * @return 생성된 마라톤 ID
     */
    @PostMapping
    @Operation(summary = "마라톤 정보 등록", description = "마라톤 관련 이미지를 포함한 정보를 저장합니다.")
    public ResponseEntity<BooleanResponse> createMarathon(
            @RequestPart @Valid CreateMarathonRequest createMarathonRequest,
            @RequestPart MultipartFile thumbnailFile,
            @RequestPart MultipartFile infoImageFile,
            @RequestPart MultipartFile courseImageFile) {

        marathonService.createMarathon(createMarathonRequest, thumbnailFile, infoImageFile, courseImageFile);

        return ResponseEntity.ok(BooleanResponse.success());
    }
}