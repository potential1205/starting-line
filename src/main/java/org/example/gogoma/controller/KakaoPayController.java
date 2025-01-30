package org.example.gogoma.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.example.gogoma.external.kakao.pay.ApproveRequest;
import org.example.gogoma.external.kakao.pay.ApproveResponse;
import org.example.gogoma.external.kakao.pay.ReadyRequest;
import org.example.gogoma.external.kakao.pay.ReadyResponse;
import org.example.gogoma.external.kakao.pay.KakaoPayClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usermarathon/pay/kakao")
@RequiredArgsConstructor
public class KakaoPayController {

    private final KakaoPayClient kakaoPayClient;

    /**
     * 결제 준비 화면
     * @Body readyRequest
     * @return readyResponse(tid, nextRedirectUrl)
     */
    @PostMapping("/ready")
    @Operation(summary = "카카오페이 결제 준비", description = "유저 ID, 대회 ID, 판매품목 이름, 가격을 받아와 카카오페이 결제 tid와 리다이렉트 주소를 반환합니다.")
    public ResponseEntity<ReadyResponse> readyPayment(@RequestBody ReadyRequest readyRequest) {
        ReadyResponse readyResponse = kakaoPayClient.preparePayment(readyRequest);
        return ResponseEntity.ok(readyResponse);
    }

    /**
     * 결제 승인 화면
     * @Body approveRequest
     * @return 결제 정보
     */
    @PostMapping("/approve")
    @Operation(summary = "카카오페이 결제 승인", description = "유저 ID, 대회 ID, tid, pgToken을 받아와 결제를 승인하고 결제 정보를 반환합니다.")
    public ResponseEntity<ApproveResponse> approvePayment(@RequestBody ApproveRequest approveRequest) {
        ApproveResponse approveResponse = kakaoPayClient.approvePayment(approveRequest);
        return ResponseEntity.ok(approveResponse);
    }
}