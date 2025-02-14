package org.example.gogoma.domain.usermarathon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.gogoma.domain.marathon.entity.Marathon;
import org.example.gogoma.domain.usermarathon.enums.PaymentType;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMarathonDetailDto {

    private Marathon marathon;
    private List<String> courseTypeList;
    private PaymentType paymentType;
    private String paymentAmount;
    private LocalDateTime paymentDateTime;
    private String address;
    private String selectedCourseType;
    private int targetPace;
}
