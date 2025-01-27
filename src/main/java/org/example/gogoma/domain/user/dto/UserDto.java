package org.example.gogoma.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String name;
    private String email;

    public static UserDto of(String email, String name) {
        return UserDto.builder()
                .email(email)
                .name(name)
                .build();
    }
}
