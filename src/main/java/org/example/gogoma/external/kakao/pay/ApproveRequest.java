package org.example.gogoma.external.kakao.pay;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ApproveRequest {

    private String userId;
    private String orderId;
    private String tid;
    private String pgToken;
}