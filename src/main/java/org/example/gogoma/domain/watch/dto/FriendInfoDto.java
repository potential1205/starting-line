package org.example.gogoma.domain.watch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendInfoDto {

    private int userId;
    private String userName;
}
