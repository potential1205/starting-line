package org.example.gogoma.external.bank;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Header {

    @JsonProperty("Trtm")
    private String trtm;

    @JsonProperty("Rsms")
    private String rsms;

    @JsonProperty("ApiNm")
    private String apiNm;

    @JsonProperty("IsTuno")
    private String isTuno;

    @JsonProperty("Tsymd")
    private String tsymd;

    @JsonProperty("FintechApsno")
    private String fintechApsno;

    @JsonProperty("Iscd")
    private String iscd;

    @JsonProperty("Rpcd")
    private String rpcd;

    @JsonProperty("ApiSvcCd")
    private String apiSvcCd;
}
