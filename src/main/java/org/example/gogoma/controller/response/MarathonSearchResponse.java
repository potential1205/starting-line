package org.example.gogoma.controller.response;

import lombok.Builder;
import lombok.Getter;
import org.example.gogoma.domain.marathon.dto.MarathonPreviewDto;

import java.util.List;

@Getter
@Builder
public class MarathonSearchResponse {

    private List<MarathonPreviewDto> marathonPreviewDtoList;

    public static MarathonSearchResponse of(List<MarathonPreviewDto> marathonPreviewDtoList) {
        return MarathonSearchResponse.builder()
                .marathonPreviewDtoList(marathonPreviewDtoList)
                .build();
    }
}
