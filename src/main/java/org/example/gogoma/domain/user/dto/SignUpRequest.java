package org.example.gogoma.domain.user.dto;

import lombok.Data;
import org.example.gogoma.domain.user.enums.Gender;

@Data
public class SignUpRequest {
    private String nickname;
    private String profileImage;
    private String email;
    private String name;
    private Gender gender;
    private String birthDate;
    private String birthYear;
    private String phoneNumber;
    private String address;
    private String clothingSize;
}