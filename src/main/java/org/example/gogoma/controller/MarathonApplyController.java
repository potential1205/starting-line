package org.example.gogoma.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.example.gogoma.common.dto.BooleanResponse;
import org.example.gogoma.controller.request.MarathonApplyRequest;
import org.example.gogoma.domain.marathon.service.MarathonApplyService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usermarathon")
@RequiredArgsConstructor
public class MarathonApplyController {

    private final MarathonApplyService marathonApplyService;

    /**
     * 마라톤 폼 신청 API
     */
    @PostMapping("/apply")
    @Operation(summary = "마라톤 신청", description = "사용자가 입력한 정보를 바탕으로 마라톤 신청 폼을 자동으로 작성합니다.")
    public ResponseEntity<BooleanResponse> applyMarathon(@RequestBody MarathonApplyRequest request) {
        marathonApplyService.applyMarathon(
                request.getUserApplyMarathonDto(),
                request.getMarathonApplyUrl(),
                request.getFormNumber()
        );
        return ResponseEntity.ok(new BooleanResponse(true));
    }
}