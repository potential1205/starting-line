package org.example.gogoma.external.kakao.oauth;

import org.example.gogoma.domain.user.enums.Gender;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

import java.util.Map;

@Data
public class KakaoUserInfo {
    private String profileImage;
    private String email;
    private String name;
    private Gender gender;
    private String birthDate;
    private String birthYear;
    private String phoneNumber;

    @JsonSetter("properties")
    public void setProperties(Map<String, Object> properties) {
        this.profileImage = (String) properties.get("profile_image");
    }

    @JsonSetter("kakao_account")
    public void setKakaoAccount(Map<String, Object> kakaoAccount) {
        this.email = (String) kakaoAccount.get("email");
        this.name = (String) kakaoAccount.get("name");
        this.gender = parseGender((String) kakaoAccount.get("gender"));
        this.birthDate = (String) kakaoAccount.get("birthday");
        this.birthYear = (String) kakaoAccount.get("birthyear");
        this.phoneNumber = (String) kakaoAccount.get("phone_number");
    }

    private Gender parseGender(String gender) {
        if ("male".equalsIgnoreCase(gender)) {
            return Gender.MALE;
        } else if ("female".equalsIgnoreCase(gender)) {
            return Gender.FEMALE;
        }
        return Gender.NONE;
    }
}
