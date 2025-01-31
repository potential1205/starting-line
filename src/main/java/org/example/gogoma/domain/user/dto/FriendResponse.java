package org.example.gogoma.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FriendResponse {

        @JsonProperty("id")
        private Long id;

        @JsonProperty("uuid")
        private String uuid;

        @JsonProperty("profile_nickname")
        private String profileNickname;

        @JsonProperty("profile_thumbnail_image")
        private String profileThumbnailImage;
}
