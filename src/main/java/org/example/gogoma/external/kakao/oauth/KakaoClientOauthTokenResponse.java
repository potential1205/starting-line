package org.example.gogoma.external.kakao.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KakaoClientOauthTokenResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    private String status;

    public static KakaoClientOauthTokenResponse of(String accessToken, String refreshToken, String status) {
        return KakaoClientOauthTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .status(status)
                .build();
    }
}
