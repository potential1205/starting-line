package org.example.gogoma.external.bank;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.gogoma.external.bank.Header;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceivedTransferResponse {

    @JsonProperty("Header")
    private Header header;
}
