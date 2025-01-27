package org.example.gogoma.external.kakao.pay;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class KakaoPayClient {

    private final WebClient webClient;
}
