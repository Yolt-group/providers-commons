package com.yolt.providers.common.pis.paymentexecutioncontext.http;

@FunctionalInterface
public interface PaymentExecutionHttpRequestBodyProvider<PreExecutionResult, HttpRequestBody> {

    HttpRequestBody provideHttpRequestBody(PreExecutionResult preExecutionResult);
}
