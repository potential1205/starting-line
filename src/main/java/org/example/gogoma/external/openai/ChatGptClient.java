package org.example.gogoma.external.openai;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class ChatGptClient {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    @Value("${openai.api.key}")
    private String apiKey;

    private final WebClient.Builder webClientBuilder;

    public String chat(String prompt) {

        WebClient webClient = webClientBuilder
                .baseUrl(apiURL)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();

        ChatGptRequest request = new ChatGptRequest(model, prompt);

        ChatGptResponse chatGptResponse = webClient.post()
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ChatGptResponse.class)
                .block();

        // 결과 반환
        return chatGptResponse.getChoices().get(0).getMessage().getContent();
    }
}
