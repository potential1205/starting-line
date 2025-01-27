package org.example.gogoma.controller.response;

import lombok.Builder;
import lombok.Getter;
import org.example.gogoma.domain.user.dto.UserDto;
import org.example.gogoma.domain.user.entity.User;

import java.util.List;

@Getter
@Builder
public class UserListResponse {

    private List<UserDto> userDtoList;

    public static UserListResponse of(List<User> users) {

        List<UserDto> userResponses = users.stream()
                .map(user -> UserDto.of(user.getEmail(), user.getName()))
                .toList();

        return UserListResponse.builder()
                .userDtoList(userResponses)
                .build();
    }
}
