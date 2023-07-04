package com.yolt.providers.common.pis.paymentexecutioncontext.model;

import lombok.Value;

import java.time.Instant;

@Value
public class PaymentExecutionContextMetadata {

    private Instant requestTimestamp;
    private Instant responseTimestamp;
    private String rawRequestBody;
    private String rawResponseBody;
    private String rawRequestHeaders;
    private String rawResponseHeaders;
    private PaymentStatuses paymentStatuses;
}
