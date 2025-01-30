package org.example.gogoma.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.gogoma.domain.user.dto.CreateUserRequest;
import org.example.gogoma.domain.user.enums.Gender;
import org.example.gogoma.external.kakao.oauth.KakaoUserInfo;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="users")
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String profileImage;

    private String roadAddress;

    private String detailAddress;

    private String birthYear;

    private String birthDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String phoneNumber;

    private String clothingSize;

    private int totalDistance;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static User of(CreateUserRequest createUserRequest) {
        return User.builder()
                .name(createUserRequest.getName())
                .profileImage(createUserRequest.getProfileImage())
                .email(createUserRequest.getEmail())
                .gender(createUserRequest.getGender())
                .birthDate(createUserRequest.getBirthDate())
                .birthYear(createUserRequest.getBirthYear())
                .phoneNumber(createUserRequest.getPhoneNumber())
                .roadAddress(createUserRequest.getRoadAddress())
                .detailAddress(createUserRequest.getDetailAddress())
                .clothingSize(createUserRequest.getClothingSize())
                .totalDistance(0)
                .build();
    }

    public static User updateWhenLogin(User existingUser, KakaoUserInfo kakaoUserInfo) {
        return User.builder()
                .name(kakaoUserInfo.getName() != null ? kakaoUserInfo.getName() : existingUser.getName())
                .profileImage(kakaoUserInfo.getProfileImage() != null ? kakaoUserInfo.getProfileImage() : existingUser.getProfileImage())
                .email(kakaoUserInfo.getEmail() != null ? kakaoUserInfo.getEmail() : existingUser.getEmail())
                .gender(kakaoUserInfo.getGender() != null ? kakaoUserInfo.getGender() : existingUser.getGender())
                .birthDate(kakaoUserInfo.getBirthDate() != null ? kakaoUserInfo.getBirthDate() : existingUser.getBirthDate())
                .birthYear(kakaoUserInfo.getBirthYear() != null ? kakaoUserInfo.getBirthYear() : existingUser.getBirthYear())
                .phoneNumber(kakaoUserInfo.getPhoneNumber() != null ? kakaoUserInfo.getPhoneNumber() : existingUser.getPhoneNumber())
                .roadAddress(existingUser.getRoadAddress())
                .detailAddress(existingUser.getDetailAddress())
                .clothingSize(existingUser.getClothingSize())
                .build();
    }
}
