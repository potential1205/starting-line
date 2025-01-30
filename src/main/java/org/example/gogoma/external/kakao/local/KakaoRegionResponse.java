package org.example.gogoma.external.kakao.local;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class KakaoRegionResponse {
    private Meta meta;
    private List<Document> documents;

    @Getter
    @Setter
    public static class Meta {
        @JsonProperty("total_count")
        private int totalCount;
    }

    @Getter
    @Setter
    public static class Document {
        @JsonProperty("region_type")
        private String regionType;

        @JsonProperty("address_name")
        private String addressName;

        @JsonProperty("region_1depth_name")
        private String region1DepthName; // 시/도 단위

        @JsonProperty("region_2depth_name")
        private String region2DepthName; // 구 단위

        @JsonProperty("region_3depth_name")
        private String region3DepthName; // 동 단위

        @JsonProperty("region_4depth_name")
        private String region4DepthName; // 리 단위 (법정동)

        private String code;
        private double x;
        private double y;
    }
}
