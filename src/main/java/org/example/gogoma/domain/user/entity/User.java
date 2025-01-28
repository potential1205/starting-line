package org.example.gogoma.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.gogoma.domain.user.dto.SignUpRequest;
import org.example.gogoma.domain.user.enums.Gender;
import org.example.gogoma.external.kakao.oauth.KakaoUserInfo;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String profileImage;

    private String address;

    private String birthYear;

    private String birthDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String phoneNumber;

    private String clothingSize;

    private int totalDistance;

    private LocalDateTime createdAt;

    public static User of(SignUpRequest request) {
        return User.builder()
                .name(request.getName())
                .profileImage(request.getProfileImage())
                .email(request.getEmail())
                .name(request.getName())
                .gender(request.getGender())
                .birthDate(request.getBirthDate())
                .birthYear(request.getBirthYear())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .clothingSize(request.getClothingSize())
                .totalDistance(0)
                .build();
    }

    public static User updateWhenLogin(User existingUser, KakaoUserInfo kakaoUserInfo) {
        return User.builder()
                .name(kakaoUserInfo.getNickname() != null ? kakaoUserInfo.getNickname() : existingUser.getName())
                .profileImage(kakaoUserInfo.getProfileImage() != null ? kakaoUserInfo.getProfileImage() : existingUser.getProfileImage())
                .email(kakaoUserInfo.getEmail() != null ? kakaoUserInfo.getEmail() : existingUser.getEmail())
                .name(kakaoUserInfo.getName() != null ? kakaoUserInfo.getName() : existingUser.getName())
                .gender(kakaoUserInfo.getGender() != null ? kakaoUserInfo.getGender() : existingUser.getGender())
                .birthDate(kakaoUserInfo.getBirthDate() != null ? kakaoUserInfo.getBirthDate() : existingUser.getBirthDate())
                .birthYear(kakaoUserInfo.getBirthYear() != null ? kakaoUserInfo.getBirthYear() : existingUser.getBirthYear())
                .phoneNumber(kakaoUserInfo.getPhoneNumber() != null ? kakaoUserInfo.getPhoneNumber() : existingUser.getPhoneNumber())
                .address(existingUser.getAddress())
                .clothingSize(existingUser.getClothingSize())
                .build();
    }
}
