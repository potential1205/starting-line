package org.example.gogoma.domain.gift.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="user_gift_options")
public class UserGiftOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(nullable = false)
    private int giftId;

    @NotNull
    @Column(nullable = false)
    private int giftOptionId;

    @NotNull
    @Column(nullable = false)
    private int userId;
}
