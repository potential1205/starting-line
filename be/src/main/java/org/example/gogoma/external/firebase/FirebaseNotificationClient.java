package org.example.gogoma.external.firebase;


import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import org.example.gogoma.domain.user.dto.FcmRequest;
import org.example.gogoma.domain.user.dto.NotificationRequest;
import org.example.gogoma.exception.ExceptionCode;
import org.example.gogoma.exception.type.ExternalApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class FirebaseNotificationClient {

    private static final String PROJECT_ID = "gogomarathon-b07af";

    private static final String FCM_API_URL = "https://fcm.googleapis.com/v1/projects/" + PROJECT_ID + "/messages:send";

    private final WebClient webClient = WebClient.builder().baseUrl(FCM_API_URL).defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();

    @Value("${firebase.serviceAccountKey}")
    private String serviceAccountKey;

    private String getAccessToken()  {
        try {
            InputStream serviceAccountStream = new ByteArrayInputStream(serviceAccountKey.getBytes(StandardCharsets.UTF_8));
            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(serviceAccountStream)
                    .createScoped("https://www.googleapis.com/auth/firebase.messaging");
            googleCredentials.refreshIfExpired();
            return googleCredentials.getAccessToken().getTokenValue();
        } catch (IOException e) {
            throw new ExternalApiException(ExceptionCode.EXTERNAL_API_ERROR);
        }
    }

    public void sendPushNotification(FcmRequest fcmRequest, String token) {
        String accessToken = getAccessToken();
        webClient.post()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .bodyValue(NotificationRequest.of(fcmRequest, token))
                .retrieve()
                .bodyToMono(String.class)
                .subscribe();
    }

}
