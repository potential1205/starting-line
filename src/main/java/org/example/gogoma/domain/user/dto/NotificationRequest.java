package org.example.gogoma.domain.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationRequest {
    private final Message message;

    public static NotificationRequest of(FcmRequest fcmRequest, String fcmToken) {
        String bodyMessage = String.format("%s님이 %s 대회에 신청했습니다!", fcmRequest.getUsername(), fcmRequest.getMarathonName());
        return NotificationRequest.builder()
                .message(Message.builder()
                        .token(fcmToken)
                        .notification(Notification.builder()
                                .title("꼬마 알림")
                                .body(bodyMessage)
                                .build())
                        .build())
                .build();
    }
}
