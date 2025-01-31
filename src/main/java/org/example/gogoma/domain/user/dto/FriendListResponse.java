package org.example.gogoma.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class FriendListResponse {

    @JsonProperty("elements")
    private List<FriendResponse> friends; // 카카오 API의 "elements"를 매핑

    @JsonProperty("total_count")
    private int totalCount;

    @JsonProperty("before_url")
    private String beforeUrl;

    @JsonProperty("after_url")
    private String afterUrl;
}
