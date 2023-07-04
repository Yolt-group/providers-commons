package com.yolt.providers.common.pis.paymentexecutioncontext.model;

import lombok.NonNull;
import lombok.Value;

import java.time.Instant;
import java.util.Optional;

@Value
public class PaymentExecutionResult<HttpResponseBody, PreExecutionResult> {

    /**
     * Will be stored in Site Management for support purposes
     */
    @NonNull
    private Instant requestTimestamp;

    /**
     * Will be stored in Site Management for support purposes
     */
    @NonNull
    private Instant responseTimestamp;

    /**
     * Raw requests / response be stored in Site Management for now for compliance, until the compliance storage target
     * solution is ready at data science. Once this solution will be done, providers could starts sending requests /
     * responses to DS compliance storage.
     */
    @NonNull
    private String rawRequestBody;

    /**
     * Raw requests / response be stored in Site Management for now for compliance, until the compliance storage target
     * solution is ready at data science. Once this solution will be done, providers could starts sending requests /
     * responses to DS compliance storage.
     */
    @NonNull
    private String rawResponseBody;

    /**
     * Raw requests / response be stored in Site Management for now for compliance, until the compliance storage target
     * solution is ready at data science. Once this solution will be done, providers could starts sending requests /
     * responses to DS compliance storage.
     */
    @NonNull
    private String rawRequestHeaders;

    /**
     * Raw requests / response be stored in Site Management for now for compliance, until the compliance storage target
     * solution is ready at data science. Once this solution will be done, providers could starts sending requests /
     * responses to DS compliance storage.
     */
    @NonNull
    private String rawResponseHeaders;

    @NonNull
    private PaymentStatuses paymentStatuses;

    private HttpResponseBody httpResponseBody;

    private PreExecutionResult preExecutionResult;

    public PaymentExecutionContextMetadata toMetadata() {
        return new PaymentExecutionContextMetadata(
                requestTimestamp,
                responseTimestamp,
                rawRequestBody,
                rawResponseBody,
                rawRequestHeaders,
                rawResponseHeaders,
                paymentStatuses
        );
    }

    public Optional<HttpResponseBody> getHttpResponseBody() {
        return Optional.ofNullable(httpResponseBody);
    }
}
