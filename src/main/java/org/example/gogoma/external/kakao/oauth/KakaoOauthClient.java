package org.example.gogoma.external.kakao.oauth;

import lombok.RequiredArgsConstructor;
import org.example.gogoma.domain.user.dto.StatusResponse;
import org.example.gogoma.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class KakaoOauthClient {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    private final WebClient webClient;
    private final UserRepository userRepository;

    private static final String TOKEN_REQUEST_URL = "https://kauth.kakao.com/oauth/token";
    private static final String USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";
    private static final String FRIEND_INFO_URL = "https://kapi.kakao.com/v1/api/talk/friends";

    public KakaoClientOauthTokenResponse determineLoginOrSignupWithWeb(String code) {
        KakaoClientOauthTokenResponse tokens = getToken(code);
        String accessToken = tokens.getAccessToken();
        String refreshToken = tokens.getRefreshToken();

        KakaoUserInfo userInfo = getUserInfo(accessToken);
        boolean isUserExists = userRepository.findByEmail(userInfo.getEmail()).isPresent();

        String status = isUserExists ? "login" : "signup";
        return KakaoClientOauthTokenResponse.of(accessToken, refreshToken, status);
    }

    public StatusResponse determineLoginOrSignup(String accessToken) {

        KakaoUserInfo userInfo = getUserInfo(accessToken);
        boolean isUserExists = userRepository.findByEmail(userInfo.getEmail()).isPresent();

        String status = isUserExists ? "login" : "signup";
        return StatusResponse.of(status);
    }

    public KakaoClientOauthTokenResponse getToken(String authorizationCode) {
        String body = String.format(
                "grant_type=authorization_code&client_id=%s&redirect_uri=%s&code=%s",
                clientId, redirectUri, authorizationCode
        );

        return webClient.post()
                .uri(TOKEN_REQUEST_URL)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(KakaoClientOauthTokenResponse.class)
                .block();
    }

    public KakaoUserInfo getUserInfo(String accessToken) {
        return webClient.get()
                .uri(USER_INFO_URL)
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(KakaoUserInfo.class)
                .block();
    }

    public KakaoFriendListResponse getFriendList(String accessToken) {
        return webClient.get()
                .uri(FRIEND_INFO_URL)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(KakaoFriendListResponse.class)
                .block();
    }

    public KakaoClientOauthTokenResponse refreshAccessToken(String refreshToken) {
        String body = String.format(
                "grant_type=refresh_token&client_id=%s&refresh_token=%s",
                clientId, refreshToken
        );

        return webClient.post()
                .uri(TOKEN_REQUEST_URL)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(KakaoClientOauthTokenResponse.class)
                .block();
    }

    public void unlinkKakao(String accessToken) {
        webClient.post()
                .uri("/v1/user/unlink")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve();
    }

    public HttpHeaders generateRedirectUri(String code) {
        String redirectUrl = "gogoma://oauth?code=" + URLEncoder.encode(code, StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", redirectUrl);
        return headers;
    }

}
