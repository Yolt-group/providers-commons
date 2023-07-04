package com.yolt.providers.common.pis.paymentexecutioncontext.model;

@FunctionalInterface
public interface PaymentIdExtractor<HttpResponseBody, PreExecutionResult> {

    String extractPaymentId(HttpResponseBody httpResponseBody, PreExecutionResult preExecutionResult);
}
