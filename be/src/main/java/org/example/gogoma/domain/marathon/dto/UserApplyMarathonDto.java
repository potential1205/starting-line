package org.example.gogoma.domain.marathon.dto;

import lombok.*;
import org.example.gogoma.domain.user.enums.Gender;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserApplyMarathonDto {
    private String marathonType;    // 종목
    private String name;
    private String email;
    private Gender gender;
    private String birthYear;
    private String birthMonth;
    private String birthDay;
    private String roadAddress;
    private String detailAddress;
    private String phoneNumber;
    private String clothingSize;
    private String giftOption;
    @Builder.Default
    private String password = "ggm123";

    public String getGenderAsString() {
        return this.gender == Gender.FEMALE ? "2" : "1";
    }

    public String getBirthMonthAsNumber() {
        return String.valueOf(Integer.parseInt(this.birthMonth));
    }

    public String getBirthDayAsNumber() {
        return String.valueOf(Integer.parseInt(this.birthDay));
    }
}

