package org.example.gogoma.domain.usermarathon.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.gogoma.domain.usermarathon.enums.PaymentType;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="user_marathons")
public class UserMarathon {

    @Id
    private int id;

    @NotNull
    @Column(nullable = false)
    private int userId;

    @NotNull
    @Column(nullable = false)
    private int marathonId;

    private String address;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    private String paymentAmount;

    private LocalTime raceTime;
}
