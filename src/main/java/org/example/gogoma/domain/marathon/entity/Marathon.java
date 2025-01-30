package org.example.gogoma.domain.marathon.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.gogoma.domain.marathon.enums.MarathonStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="marathons")
public class Marathon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private LocalDateTime registrationStartDateTime;

    private LocalDateTime registrationEndDateTime;

    private LocalDateTime raceStartTime;

    private String accountBank;

    private String accountNumber;

    private String accountName;

    private String location;

    private String city;

    private String year;

    private String month;

    private String region;

    private String district;

    @ElementCollection
    @Builder.Default
    private List<String> hostList = new ArrayList<>();

    @ElementCollection
    @Builder.Default
    private List<String> organizerList = new ArrayList<>();

    @ElementCollection
    @Builder.Default
    private List<String> sponsorList = new ArrayList<>();

    private String qualifications;

    @Enumerated(EnumType.STRING)
    private MarathonStatus marathonStatus;

    private int viewCount;

    private String thumbnailImage;

    private String infoImage;

    private String courseImage;

    private int formType;

    private String formUrl;

    public void update(String title, LocalDateTime registrationStartDateTime, LocalDateTime registrationEndDateTime, LocalDateTime raceStartTime,
                       String accountBank, String accountNumber, String accountName, String location, String qualifications,
                       String year, String month, String city, String region, String district, int formType, String formUrl,
                       List<String> hostList, List<String> organizerList, List<String> sponsorList, MarathonStatus marathonStatus) {

        this.title = title;
        this.registrationStartDateTime = registrationStartDateTime;
        this.registrationEndDateTime = registrationEndDateTime;
        this.raceStartTime = raceStartTime;
        this.accountBank = accountBank;
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.location = location;
        this.qualifications = qualifications;
        this.year = year;
        this.month = month;
        this.city = city;
        this.region = region;
        this.district = district;
        this.formType = formType;
        this.formUrl = formUrl;
        this.hostList = hostList;
        this.organizerList = organizerList;
        this.sponsorList = sponsorList;
        this.marathonStatus = marathonStatus;

    }
}
