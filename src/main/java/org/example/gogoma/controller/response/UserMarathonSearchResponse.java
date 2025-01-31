package org.example.gogoma.controller.response;

import lombok.Builder;
import lombok.Getter;
import org.example.gogoma.domain.usermarathon.dto.UserMarathonSearchDto;

import java.util.List;

@Getter
@Builder
public class UserMarathonSearchResponse {
    private List<UserMarathonSearchDto> userMarathonSearchDtoList;

    public static UserMarathonSearchResponse of(List<UserMarathonSearchDto> userMarathonSearchDtoList) {
        return UserMarathonSearchResponse.builder()
                .userMarathonSearchDtoList(userMarathonSearchDtoList)
                .build();
    }
}
