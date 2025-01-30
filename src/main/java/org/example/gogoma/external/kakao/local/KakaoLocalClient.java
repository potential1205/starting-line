package org.example.gogoma.external.kakao.local;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class KakaoLocalClient {

    @Value("${kakao.client-id}")
    private String apiKey;

    // 키워드로 장소 검색
    public KakaoAddressResponse getCoordinates(String address) {
        return WebClient.builder()
                .baseUrl("https://dapi.kakao.com/v2/local/search/keyword.json")
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("query", address)
                        .build())
                .header("Authorization", "KakaoAK " + apiKey)
                .retrieve()
                .bodyToMono(KakaoAddressResponse.class)
                .block();
    }

    // 좌표로 행정구역 검색
    public KakaoRegionResponse getRegionFromCoordinates(double longitude, double latitude) {
        return WebClient.builder()
                .baseUrl("https://dapi.kakao.com/v2/local/geo/coord2regioncode.json")
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("x", longitude)
                        .queryParam("y", latitude)
                        .queryParam("input_coord", "WGS84")  // 기본 좌표계 사용
                        .build())
                .header("Authorization", "KakaoAK " + apiKey)
                .retrieve()
                .bodyToMono(KakaoRegionResponse.class)
                .block();
    }
}
