package org.example.gogoma.external.bank;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.example.gogoma.common.util.PayUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NHBankClient {

    private final WebClient webClient;
    private final PayUtil payUtil;

    @Value("${nhbank.base-url}")
    private String baseUrl;

    @Value("${nhbank.iscd}")
    private String iscd;

    @Value("${nhbank.access-token}")
    private String accessToken;

    @Value("${nhbank.fin-acno}")
    private String finAcno; // 계좌번호 (고정)

    @Value("${nhbank.acno}")
    private String myAcno; // 계좌번호 (고정)

    public Mono<ReceivedTransferResponse> requestReceivedTransfer(ReceivedTransferRequest receivedTransferRequest) {
        String url = baseUrl + "/ReceivedTransferAccountNumber.nh";

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
        String isTuno = today + payUtil.generateRandomNumber(12);
        String bncd = receivedTransferRequest.getAccountBank().equals("농협") ? "011": "012";
        String acno = receivedTransferRequest.getAccountNumber();

        // 요청 JSON 데이터 구성
        Map<String, Object> request = new HashMap<>();
        request.put("Header", Map.of(
                "ApiNm", "ReceivedTransferAccountNumber",
                "Tsymd", today,
                "Trtm", currentTime,
                "Iscd", iscd,
                "FintechApsno", "001",
                "ApiSvcCd", "ReceivedTransferA",
                "IsTuno", isTuno,
                "AccessToken", accessToken
        ));
        request.put("Bncd", "011");
        request.put("Acno", "3020000012312");
        request.put("Tram", receivedTransferRequest.getTram());
        request.put("DractOtlt", receivedTransferRequest.getName());
        request.put("MractOtlt", receivedTransferRequest.getAccountName());

        return webClient.post()
                .uri(url)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ReceivedTransferResponse.class);
    }

    public Mono<TransactionHistoryResponse> getTransactionHistory() {
        String url = baseUrl + "/InquireTransactionHistory.nh";

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String firstDayOfMonth = YearMonth.now().atDay(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
        String isTuno = today + payUtil.generateRandomNumber(12);


        Map<String, Object> request = new HashMap<>();
        request.put("Header", Map.of(
                "ApiNm", "InquireTransactionHistory",
                "Tsymd", today,
                "Trtm", currentTime,
                "Iscd", iscd,
                "FintechApsno", "001",
                "ApiSvcCd", "ReceivedTransferA",
                "IsTuno", isTuno,
                "AccessToken", accessToken
        ));
        request.put("Bncd", "011");
        request.put("Acno", myAcno);
        request.put("Insymd", firstDayOfMonth);
        request.put("Ineymd", today);
        request.put("TrnsDsnc", "A");
        request.put("Lnsq", "ASC");
        request.put("PageNo", "1");
        request.put("Dmcnt", "5");

        return webClient.post()
                .uri(url)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(TransactionHistoryResponse.class);
    }

}