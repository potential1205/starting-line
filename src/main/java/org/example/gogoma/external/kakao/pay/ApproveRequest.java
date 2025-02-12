package org.example.gogoma.external.kakao.pay;

import lombok.Data;

@Data
public class ApproveRequest {

    private String orderId;
    private String tid;
    private String pgToken;
}