package org.example.gogoma.domain.marathon.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.gogoma.domain.marathon.enums.MarathonStatus;
import org.example.gogoma.domain.marathon.enums.MarathonType;

import java.time.LocalDateTime;
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

    @NotNull
    @Column(nullable = false)
    private String title;

    private LocalDateTime registrationStartDateTime;

    private LocalDateTime registrationEndDateTime;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime raceStartTime;

    @NotNull
    @Column(nullable = false)
    private String accountNumber;

    @NotNull
    @Column(nullable = false)
    private String accountName;

    @NotNull
    @Column(nullable = false)
    private String location;

    @ElementCollection
    private List<String> hostList;

    @ElementCollection
    private List<String> organizerList;

    @ElementCollection
    private List<String> sponsorList;

    private String qualifications;

    @Enumerated(EnumType.STRING)
    private MarathonStatus marathonStatus;

    @ElementCollection
    private List<MarathonType> marathonTypeList;

    private int viewCount;

    private String thumbnailImage;

    private String infoImage;

    private String courseImage;

    private String description;
}
