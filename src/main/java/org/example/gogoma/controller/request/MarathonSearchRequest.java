package org.example.gogoma.controller.request;


import lombok.*;
import org.example.gogoma.domain.marathon.enums.MarathonStatus;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarathonSearchRequest {

    private MarathonStatus marathonStatus; // OPEN, CLOSED, FINISHED
    private String city;                   // 시/도 (예: 서울, 부산)
    private String year;
    private String month;
    private List<String> courseTypeList;              // 종목 (예: 5km, 10km, half, full)
    private String keyword;
}
