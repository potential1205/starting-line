package org.example.gogoma.external.kakao.pay;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoPayClient {

    private final WebClient webClient = WebClient.builder().baseUrl("https://open-api.kakaopay.com/online/v1/payment").build();

    @Value("${kakao.pay.secret-key}")
    private String secretKey;

    @Value("${domain.name}")
    private String serverBaseUrl;

    @Value("${app.redirect-url}")
    private String appRedirectUrl;

    public ReadyResponse preparePayment(ReadyRequest paymentRequest) {
        Map<String, String> params = new HashMap<>();
        params.put("cid", "TC0ONETIME");
        params.put("partner_order_id", paymentRequest.getOrderId());
        params.put("partner_user_id", paymentRequest.getUserId());
        params.put("item_name", paymentRequest.getItemName());
        params.put("quantity", "1");
        params.put("total_amount", paymentRequest.getTotalAmount());
        params.put("tax_free_amount", "0");
        String redirectUrl = "api/v1/usermarathons/pay/kakao/redirect?redirect=";
        params.put("approval_url", serverBaseUrl + redirectUrl + appRedirectUrl + "success");
        params.put("cancel_url", serverBaseUrl + redirectUrl + appRedirectUrl + "cancel");
        params.put("fail_url", serverBaseUrl + redirectUrl + appRedirectUrl + "fail");


        return webClient.post()
                .uri("/ready")
                .header(HttpHeaders.AUTHORIZATION, secretKey)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(params)
                .retrieve()
                .bodyToMono(ReadyResponse.class)
                .block();
    }

    public ApproveResponse approvePayment(ApproveRequest approveRequest) {

        Map<String, String> params = new HashMap<>();
        params.put("cid", "TC0ONETIME");
        params.put("tid", approveRequest.getTid());
        params.put("partner_order_id", approveRequest.getOrderId());
        params.put("partner_user_id", approveRequest.getUserId());
        params.put("pg_token", approveRequest.getPgToken());

        return webClient.post()
                .uri("/approve")
                .header(HttpHeaders.AUTHORIZATION, secretKey)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(params)
                .retrieve()
                .bodyToMono(ApproveResponse.class)
                .block();
    }

}
