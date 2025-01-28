package org.example.gogoma.external.kakao.oauth;

import lombok.Data;
import org.example.gogoma.domain.user.enums.Gender;

@Data
public class KakaoUserInfo {
    private String nickname;
    private String profileImage;
    private String email;
    private String name;
    private Gender gender;
    private String birthDate;
    private String birthYear;
    private String phoneNumber;

}