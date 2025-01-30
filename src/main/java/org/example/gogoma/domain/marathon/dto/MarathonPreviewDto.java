package org.example.gogoma.domain.marathon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.gogoma.domain.marathon.entity.MarathonType;
import org.example.gogoma.domain.marathon.enums.MarathonStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarathonPreviewDto {

    private int id;
    private String title;
    private LocalDateTime registrationStartDateTime;
    private LocalDateTime registrationEndDateTime;
    private LocalDateTime raceStartTime;
    private String location;
    private String city;
    private String region;
    private String district;
    private MarathonStatus marathonStatus;
    private String thumbnailImage;
    private String dDay;
    private List<String> courseTypeList;
    private List<MarathonType> marathonTypeList;
}
