package com.yolt.providers.common.pis.sepa;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class SepaInitiatePaymentResponseDTO {

    @JsonProperty("_links")
    private final SepaLinksDTO links;

    private final SepaPaymentStatusResponseDTO paymentStatus;
}
