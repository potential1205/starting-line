package org.example.gogoma.controller.request;

import lombok.*;
import org.example.gogoma.domain.marathon.entity.Marathon;
import org.example.gogoma.domain.marathon.entity.MarathonType;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarathonDetailRequest {
    private Marathon marathon;  // 마라톤 기본 정보
    private List<MarathonType> marathonTypeList; // 마라톤 종목 리스트
}
