package org.example.gogoma.controller.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMarathonRequest {

    private String thumbnailUrl; // 썸네일 이미지 URL

    private String infoImageUrl; // 모집 요강 이미지 URL

    private String courseImageUrl; // 마라톤 코스 이미지 URL

    private String textInfo; // 마라톤 관련 기타 텍스트 정보
}
