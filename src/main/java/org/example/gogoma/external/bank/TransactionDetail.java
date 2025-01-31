package org.example.gogoma.external.bank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDetail {
    private String Trdd;     // 거래일자
    private String Txtm;     // 거래시간
    private String MnrcDrotDsnc; // 입출금 구분 (3: 입금, 4: 출금 등)
    private String Tram;     // 거래금액
    private String AftrBlnc; // 거래 후 잔액
    private String TrnsAfAcntBlncSmblCd; // 잔액 기호 (+, -)
    private String Smr;      // 적요 (설명)
    private String HnisCd;   // 행내코드
    private String HnbrCd;   // 행내 점포코드
    private String Ccyn;     // 통화코드
    private String Tuno;     // 거래번호
    private String BnprCntn; // 은행 제공 내용
}
