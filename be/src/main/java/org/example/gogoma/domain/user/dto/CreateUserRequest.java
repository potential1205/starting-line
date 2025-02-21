package org.example.gogoma.domain.user.dto;

import lombok.Data;
import org.example.gogoma.domain.user.enums.Gender;

@Data
public class CreateUserRequest {

    private Long kakaoId;
    private String name;
    private String profileImage;
    private String email;
    private Gender gender;
    private String birthDate;
    private String birthYear;
    private String phoneNumber;
    private String roadAddress;
    private String detailAddress;
    private String clothingSize;
    private String fcmToken;
}