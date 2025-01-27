package org.example.gogoma.controller.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

    private String name;
    private String email;

    public static UserResponse of(String email, String name) {

        return UserResponse.builder()
                .email(email)
                .name(name)
                .build();
    }
}

