package org.example.gogoma.controller.response;

import lombok.Builder;
import lombok.Getter;
import org.example.gogoma.domain.user.entity.User;

import java.util.List;

@Getter
@Builder
public class UserListResponse {

    private List<UserResponse> userList;

    public static UserListResponse of(List<User> users) {

        List<UserResponse> userResponses = users.stream()
                .map(UserResponse::of)
                .toList();

        return UserListResponse.builder()
                .userList(userResponses)
                .build();
    }
}
