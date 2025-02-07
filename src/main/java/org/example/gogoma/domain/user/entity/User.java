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
    private Long kakaoId;

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

    private String fcmToken;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static User of(CreateUserRequest createUserRequest) {
        return User.builder()
                .kakaoId(createUserRequest.getKakaoId())
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
                .fcmToken(createUserRequest.getFcmToken())
                .totalDistance(0)

                .build();
    }

    public static void updateWhenLogin(User existingUser, KakaoUserInfo kakaoUserInfo) {
        if (kakaoUserInfo.getKakaoId() != null) existingUser.setKakaoId(kakaoUserInfo.getKakaoId());
        if (kakaoUserInfo.getName() != null) existingUser.setName(kakaoUserInfo.getName());
        if (kakaoUserInfo.getProfileImage() != null) existingUser.setProfileImage(kakaoUserInfo.getProfileImage());
        if (kakaoUserInfo.getEmail() != null) existingUser.setEmail(kakaoUserInfo.getEmail());
        if (kakaoUserInfo.getGender() != null) existingUser.setGender(kakaoUserInfo.getGender());
        if (kakaoUserInfo.getBirthDate() != null) existingUser.setBirthDate(kakaoUserInfo.getBirthDate());
        if (kakaoUserInfo.getBirthYear() != null) existingUser.setBirthYear(kakaoUserInfo.getBirthYear());
        if (kakaoUserInfo.getPhoneNumber() != null) existingUser.setPhoneNumber(kakaoUserInfo.getPhoneNumber());
    }

}
