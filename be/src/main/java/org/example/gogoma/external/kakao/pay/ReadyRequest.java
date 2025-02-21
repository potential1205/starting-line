package org.example.gogoma.external.kakao.pay;

import lombok.Data;

@Data
public class ReadyRequest {

    private String orderId;
    private String itemName;
    private String totalAmount;
}