package org.example.gogoma.domain.marathon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserApplyMarathonDto {
    private String event;       // 종목
    private String souvenir;    // 기념품, 또는 옷사이즈
    private String name;        // 성명
    private String roadAddress; // 도로명 주소
    private String addressDetail;     // 상세 주소
    private String birthYear;   // 생년월일 - 년도
    private String birthMonth;  // 생년월일 - 월
    private String birthDay;    // 생년월일 - 일
    private String gender;      // 성별 (남자 -> "1", 여자 -> "2")
    private String phoneNumber;  // 전화번호
    private String email;       // 이메일
    private String depositor;   // 입금자명
    private String password;    // 비밀번호
}

