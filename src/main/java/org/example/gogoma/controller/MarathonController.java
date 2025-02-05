package org.example.gogoma.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gogoma.common.dto.BooleanResponse;
import org.example.gogoma.controller.request.CreateMarathonRequest;
import org.example.gogoma.controller.request.MarathonDetailRequest;
import org.example.gogoma.controller.request.MarathonSearchRequest;
import org.example.gogoma.controller.response.MarathonDetailResponse;
import org.example.gogoma.controller.response.MarathonSearchResponse;
import org.example.gogoma.domain.marathon.service.MarathonService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/marathons")
@CrossOrigin("*")
@Slf4j
public class MarathonController {

    private final MarathonService marathonService;

    /**
     * 마라톤 정보를 저장하는 API
     * @param createMarathonRequest 마라톤 정보를 담은 요청 객체
     * @param thumbnailFile 썸네일 이미지 파일
     * @param infoImageFile 마라톤 정보 이미지 파일
     * @param courseImageFile 마라톤 코스 이미지 파일
     * @return 마라톤 등록 성공 여부
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

    /**
     * 마라톤 정보를 조회하는 API
     * @param id 조회할 마라톤 ID
     * @return 조회된 마라톤 정보
     */
    @GetMapping("/{id}")
    @Operation(summary = "마라톤 정보 조회", description = "마라톤 ID를 이용해 마라톤 정보를 조회합니다.")
    public ResponseEntity<MarathonDetailResponse> getMarathonById(@PathVariable int id) {
        MarathonDetailResponse marathonDetailResponse = marathonService.getMarathonById(id);

        return ResponseEntity.ok(marathonDetailResponse);
    }

    /**
     * 마라톤 정보를 수정하는 API
     *
     * @param marathonDetailRequest 수정할 마라톤 정보를 담은 요청 객체
     * @return 수정 성공 여부 (true/false)
     */
    @PutMapping
    @Operation(summary = "마라톤 정보 수정", description = "마라톤 ID를 기준으로 기존 정보를 업데이트합니다.")
    public ResponseEntity<BooleanResponse> updateMarathon(
            @RequestPart @Valid MarathonDetailRequest marathonDetailRequest,
            @RequestPart MultipartFile thumbnailFile,
            @RequestPart MultipartFile infoImageFile,
            @RequestPart MultipartFile courseImageFile) {

        marathonService.updateMarathon(marathonDetailRequest, thumbnailFile, infoImageFile, courseImageFile);

        return ResponseEntity.ok(BooleanResponse.success());
    }

    /**
     * 마라톤 정보를 조회하는 API
     * @param id 조회할 마라톤 ID
     * @return 수정 성공 여부 (true/false)
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "마라톤 정보 삭제", description = "마라톤 ID를 기준으로 기존 정보를 삭제합니다.")
    public ResponseEntity<BooleanResponse> deleteMarathon(@PathVariable int id) {

        marathonService.deleteMarathon(id);

        return ResponseEntity.ok(BooleanResponse.success());
    }

    /**
     * 마라톤 목록 조회 API
     *
     * @param marathonSearchRequest 검색 조건을 담은 요청 객체
     * @return 필터링된 마라톤 목록
     */
    @GetMapping
    @Operation(summary = "마라톤 목록 조회", description = "마라톤의 상태, 지역, 연도, 월, 종목 등의 조건을 활용하여 마라톤 목록을 필터링하여 조회합니다.")
    public ResponseEntity<MarathonSearchResponse> searchMarathonList(
            @ModelAttribute @ParameterObject @Valid MarathonSearchRequest marathonSearchRequest) {

        MarathonSearchResponse marathonSearchResponse =
                marathonService.searchMarathonList(marathonSearchRequest);

        return ResponseEntity.ok(marathonSearchResponse);
    }

}