package org.example.gogoma.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.gogoma.domain.user.enums.Gender;

@Data
@AllArgsConstructor
public class ApplyResponse {

    private String name;
    private String email;
    private Gender gender;
    private String birthMonth;
    private String birthDay;
    private String birthYear;
    private String phoneNumber;
    private String roadAddress;
    private String detailAddress;
    private String clothingSize;

}
