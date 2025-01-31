package org.example.gogoma.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.gogoma.external.bank.NHBankClient;
import org.example.gogoma.external.bank.ReceivedTransferRequest;
import org.example.gogoma.external.bank.ReceivedTransferResponse;
import org.example.gogoma.external.bank.TransactionHistoryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/usermarathon/pay/bank")
@RequiredArgsConstructor
public class BankController {

    private final NHBankClient nhBankClient;

    /**
     * 출금 이체 API
     * @Body receivedTransferRequest
     * @return ReceivedTransferResponse
     */
    @PostMapping("/send")
    @Operation(summary = "출금 이체", description = "서비스 계좌에서 대회 측 계좌로 출금합니다.")
    public ResponseEntity<Mono<ReceivedTransferResponse>> receiveTransfer(@RequestBody ReceivedTransferRequest receivedTransferRequest) {
        Mono<ReceivedTransferResponse> receivedTransferResponse = nhBankClient.requestReceivedTransfer(receivedTransferRequest);
        return ResponseEntity.ok(receivedTransferResponse);
    }

    /**
     * 거래 내역 조회 API
     * @return transactionHistoryResponse
     */
    @PostMapping("/retrieve")
    @Operation(summary = "거래내역조회", description = "계좌거래내역을 확인합니다.")
    public ResponseEntity<Mono<TransactionHistoryResponse>> getTransactionHistory() {
        Mono<TransactionHistoryResponse> transactionHistoryResponse = nhBankClient.getTransactionHistory();
        return ResponseEntity.ok(transactionHistoryResponse);
    }
}