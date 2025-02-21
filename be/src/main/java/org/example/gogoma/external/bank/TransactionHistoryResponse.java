package org.example.gogoma.external.bank;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHistoryResponse {

    @JsonProperty("Header")
    private Header header; // 응답 헤더

    @JsonProperty("CtntDataYn")
    private String hasMoreData; // 추가 데이터 존재 여부 (Y/N)

    @JsonProperty("TotCnt")
    private String totalTransactionCount; // 전체 거래 건수

    @JsonProperty("Iqtcnt")
    private String queriedTransactionCount; // 조회된 거래 건수

    @JsonProperty("REC")
    private List<TransactionDetail> transactionDetails; // 거래 내역 리스트
}
