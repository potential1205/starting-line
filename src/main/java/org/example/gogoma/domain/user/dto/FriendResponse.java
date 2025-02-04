package org.example.gogoma.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendResponse {

    private int friendId;

    private String name;

    private String profileImage;

    private int totalDistance;
}
