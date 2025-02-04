package org.example.gogoma.external.kakao.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class KakaoFriendListResponse {

    @JsonProperty("elements")
    private List<KakaoFriendResponse> friends; // 카카오 API의 "elements"를 매핑

    @JsonProperty("total_count")
    private int totalCount;

    @JsonProperty("before_url")
    private String beforeUrl;

    @JsonProperty("after_url")
    private String afterUrl;
}
