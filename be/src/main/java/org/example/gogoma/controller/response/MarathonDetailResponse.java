package org.example.gogoma.controller.response;

import lombok.Builder;
import lombok.Getter;
import org.example.gogoma.domain.marathon.entity.Marathon;
import org.example.gogoma.domain.marathon.entity.MarathonType;

import java.util.List;

@Getter
@Builder
public class MarathonDetailResponse {
    private Marathon marathon;  // 마라톤 기본 정보
    private List<MarathonType> marathonTypeList; // 마라톤 종목 리스트
    private String dDay;

    public static MarathonDetailResponse of(Marathon marathon, List<MarathonType> marathonTypeList, String dDay) {
        return MarathonDetailResponse.builder()
                .marathon(marathon)
                .marathonTypeList(marathonTypeList)
                .dDay(dDay)
                .build();
    }
}
