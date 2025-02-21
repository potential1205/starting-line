package org.example.gogoma.domain.marathon.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.gogoma.domain.marathon.dto.MarathonTypeDto;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="marathon_types")
public class MarathonType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(nullable = false)
    private int marathonId;

    private int courseType;

    private String price;

    private String etc;

    public static MarathonType of(MarathonTypeDto dto, int marathonId) {
        return MarathonType.builder()
                .marathonId(marathonId)
                .courseType(dto.getCourseType())
                .price(dto.getPrice())
                .etc(dto.getEtc())
                .build();
    }
}
