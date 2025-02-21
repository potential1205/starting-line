package org.example.gogoma.external.kakao.local;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class KakaoAddressResponse {
    private Meta meta;
    private List<Document> documents;

    @Getter
    @Setter
    public static class Meta {
        private int total_count;
        private int pageable_count;
        private boolean is_end;
        private SameName same_name;
    }

    @Getter
    @Setter
    public static class SameName {
        private List<String> region;
        private String keyword;
        private String selected_region;
    }

    @Getter
    @Setter
    public static class Document {
        private String id;
        private String place_name;
        private String category_name;
        private String category_group_code;
        private String category_group_name;
        private String phone;
        private String address_name;
        private String road_address_name;
        private String x;
        private String y;
        private String place_url;
        private String distance;
    }
}
