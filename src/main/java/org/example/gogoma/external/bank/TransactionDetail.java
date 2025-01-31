package org.example.gogoma.external.bank;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDetail {

    @JsonProperty("Trdd")
    private String transactionDate; // 거래일자

    @JsonProperty("Txtm")
    private String transactionTime; // 거래시간

    @JsonProperty("MnrcDrotDsnc")
    private String transactionType; // 입출금 구분 (3: 입금, 4: 출금 등)

    @JsonProperty("Tram")
    private String transactionAmount; // 거래금액

    @JsonProperty("AftrBlnc")
    private String afterBalance; // 거래 후 잔액

    @JsonProperty("TrnsAfAcntBlncSmblCd")
    private String balanceSign; // 잔액 기호 (+, -)

    @JsonProperty("Smr")
    private String summary; // 적요 (설명)

    @JsonProperty("HnisCd")
    private String internalBankCode; // 행내 코드

    @JsonProperty("HnbrCd")
    private String branchCode; // 행내 점포코드

    @JsonProperty("Ccyn")
    private String currencyCode; // 통화코드

    @JsonProperty("Tuno")
    private String transactionNumber; // 거래번호

    @JsonProperty("BnprCntn")
    private String bankProvidedContent; // 은행 제공 내용
}
