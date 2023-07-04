package com.yolt.providers.common.pis.paymentexecutioncontext.http;

import org.springframework.http.HttpHeaders;

@FunctionalInterface
public interface PaymentExecutionHttpHeadersProvider<PreExecutionResult, HttpRequestBody> {

    HttpHeaders provideHttpHeaders(PreExecutionResult preExecutionResult, HttpRequestBody httpRequestBody);
}
