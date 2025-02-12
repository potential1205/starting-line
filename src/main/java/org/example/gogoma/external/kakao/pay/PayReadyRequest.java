package org.example.gogoma.external.kakao.pay;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PayReadyRequest {
    private int userId;
    private String orderId;
    private String itemName;
    private String totalAmount;

    public static PayReadyRequest of(int userId, ReadyRequest readyRequest){
        return PayReadyRequest.builder()
                .userId(userId)
                .orderId(readyRequest.getOrderId())
                .itemName(readyRequest.getItemName())
                .totalAmount(readyRequest.getTotalAmount())
                .build();
    }
}
