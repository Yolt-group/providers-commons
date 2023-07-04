package com.yolt.providers.common.pis.paymentexecutioncontext.model;

@FunctionalInterface
public interface PaymentAuthorizationUrlExtractor<HttpResponseBody, PreExecutionResult> {

    String extractAuthorizationUrl(HttpResponseBody httpResponseBody, PreExecutionResult preExecutionResult);

    default String extractAuthorizationUrl(PreExecutionResult preExecutionResult) {
        return extractAuthorizationUrl(null, preExecutionResult);
    }
}
