package org.example.gogoma.external.bank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceivedTransferRequest {
    private int tram;
    private String name;
    private String accountBank;
    private String accountNumber;
    private String accountName;
}
