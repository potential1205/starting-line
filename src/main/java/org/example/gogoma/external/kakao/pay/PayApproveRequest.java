package org.example.gogoma.external.kakao.pay;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PayApproveRequest {

    private int userId;
    private String orderId;
    private String tid;
    private String pgToken;

    public static PayApproveRequest of(int userId, ApproveRequest approveRequest){
        return PayApproveRequest.builder()
                .userId(userId)
                .orderId(approveRequest.getOrderId())
                .tid(approveRequest.getTid())
                .pgToken(approveRequest.getPgToken())
                .build();
    }
}
