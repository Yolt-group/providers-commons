package com.yolt.providers.common.pis.sepa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class SepaLinksDTO {

    private final String scaRedirect;
    private final String status;
}
