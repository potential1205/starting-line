package org.example.gogoma.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendListResponse {
    private int userId;
    private List<FriendResponse> friendResponseList;

    public static FriendListResponse of(int userId, List<FriendResponse> friendResponseList) {
        return FriendListResponse.builder()
                .userId(userId)
                .friendResponseList(friendResponseList)
                .build();
    }
}
