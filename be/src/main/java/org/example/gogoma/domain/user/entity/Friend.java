package org.example.gogoma.domain.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "friends",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"userId", "friendId"})
        }
)
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(nullable = false)
    private int userId;

    @NotNull
    @Column(nullable = false)
    private int friendId;

    private String friendName;

    public static Friend of(int userId, int friendId, String friendName) {
        return Friend.builder()
                .userId(userId)
                .friendId(friendId)
                .friendName(friendName)
                .build();
    }
}
