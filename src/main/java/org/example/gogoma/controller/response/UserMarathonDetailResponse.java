package org.example.gogoma.controller.response;

import lombok.Builder;
import lombok.Getter;
import org.example.gogoma.domain.usermarathon.dto.UserMarathonDetailDto;
import org.example.gogoma.domain.usermarathon.dto.UserMarathonSearchDto;

import java.util.List;

@Getter
@Builder
public class UserMarathonDetailResponse {

    private UserMarathonDetailDto userMarathonDetailDto;

    public static UserMarathonDetailResponse of(UserMarathonDetailDto userMarathonDetailDto) {
        return UserMarathonDetailResponse.builder()
                .userMarathonDetailDto(userMarathonDetailDto)
                .build();
    }
}
