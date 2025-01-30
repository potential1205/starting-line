package org.example.gogoma.external.kakao.pay;

import lombok.Data;

@Data
public class ReadyResponse {

    private String tid;
    private String nextRedirectUrl;
}