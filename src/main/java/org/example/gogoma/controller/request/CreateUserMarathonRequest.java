package org.example.gogoma.controller.request;

import lombok.*;
import org.example.gogoma.domain.usermarathon.enums.PaymentType;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserMarathonRequest {

    private int marathonId;

    private String address;

    private PaymentType paymentType;

    private String paymentAmount;

    private LocalDateTime paymentDateTime;

    private String courseType;
}
