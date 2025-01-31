package org.example.gogoma.external.bank;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Header {

    @JsonProperty("Trtm")
    private String transactionTime; // 거래 시간

    @JsonProperty("Rsms")
    private String responseMessage; // 응답 메시지

    @JsonProperty("ApiNm")
    private String apiName; // API 이름

    @JsonProperty("IsTuno")
    private String transactionUniqueNumber; // 거래 고유번호

    @JsonProperty("Tsymd")
    private String transactionDate; // 거래 날짜

    @JsonProperty("FintechApsno")
    private String fintechAppSerialNumber; // 핀테크 앱 일련번호

    @JsonProperty("Iscd")
    private String institutionCode; // 기관 코드

    @JsonProperty("Rpcd")
    private String responseCode; // 응답 코드

    @JsonProperty("ApiSvcCd")
    private String apiServiceCode; // API 서비스 코드
}
