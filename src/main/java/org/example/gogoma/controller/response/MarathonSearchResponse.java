package org.example.gogoma.controller.response;

import lombok.Builder;
import lombok.Getter;
import org.example.gogoma.domain.marathon.dto.MarathonPreviewDto;

import java.util.List;

@Getter
@Builder
public class MarathonSearchResponse {

    private List<MarathonPreviewDto> marathonPreviewDtoList;
    private List<String> cityList;
    private List<Integer> marathonTypeList;

    public static MarathonSearchResponse of(
            List<MarathonPreviewDto> marathonPreviewDtoList, List<String> cityList, List<Integer> marathonTypeList) {

        return MarathonSearchResponse.builder()
                .marathonPreviewDtoList(marathonPreviewDtoList)
                .cityList(cityList)
                .marathonTypeList(marathonTypeList)
                .build();
    }
}
