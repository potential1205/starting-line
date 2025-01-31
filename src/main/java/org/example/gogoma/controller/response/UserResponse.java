package org.example.gogoma.controller.response;

import lombok.*;
import org.example.gogoma.domain.user.entity.User;
import org.example.gogoma.domain.user.enums.Gender;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private String name;

    private String email;

    private String profileImage;

    private String roadAddress;

    private String detailAddress;

    private String birthYear;

    private String birthDate;

    private Gender gender;

    private String phoneNumber;

    private String clothingSize;

    private int totalDistance;

    private LocalDateTime createdAt;


    public static UserResponse of(User user) {
        return UserResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .profileImage(user.getProfileImage())
                .roadAddress(user.getRoadAddress())
                .detailAddress(user.getDetailAddress())
                .birthYear(user.getBirthYear())
                .birthDate(user.getBirthDate())
                .gender(user.getGender())
                .phoneNumber(user.getPhoneNumber())
                .clothingSize(user.getClothingSize())
                .totalDistance(user.getTotalDistance())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
