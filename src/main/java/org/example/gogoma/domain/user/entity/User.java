package org.example.gogoma.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.gogoma.domain.user.enums.Gender;

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

    private int totalDistance;

    private LocalDateTime createdAt;
}
