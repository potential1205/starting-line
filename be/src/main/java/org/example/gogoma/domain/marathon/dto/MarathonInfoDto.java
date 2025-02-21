package org.example.gogoma.domain.marathon.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarathonInfoDto {
    private String title;
    private String registrationStartDateTime;
    private String registrationEndDateTime;
    private String raceStartTime;
    private String accountBank;
    private String accountNumber;
    private String accountName;
    private String location;
    private List<String> hostList;
    private List<String> organizerList;
    private List<String> sponsorList;
    private String qualifications;
    private List<MarathonTypeDto> marathonTypeDtoList;
}