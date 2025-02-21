package org.example.gogoma.external.kakao.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KakaoFriendResponse {

        @JsonProperty("id")
        private Long id;

        @JsonProperty("uuid")
        private String uuid;

        @JsonProperty("profile_nickname")
        private String profileNickname;

        @JsonProperty("profile_thumbnail_image")
        private String profileThumbnailImage;
}
