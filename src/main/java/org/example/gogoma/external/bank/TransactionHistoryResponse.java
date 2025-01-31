package org.example.gogoma.external.bank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHistoryResponse {
    private Header Header;
    private String CtntDataYn;  // 추가: 데이터 존재 여부 (Y/N)
    private String TotCnt;       // 추가: 전체 거래 건수
    private String Iqtcnt;       // 추가: 조회된 거래 건수
    private List<TransactionDetail> REC; // 거래 내역 리스트
}


