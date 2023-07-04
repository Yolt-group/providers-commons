package com.yolt.providers.common.pis.paymentexecutioncontext.adapter.sepa.initiate;

@FunctionalInterface
public interface SepaPaymentProviderStateExtractor<HttpResponseBody, PreExecutionResult> {

    String extractProviderState(HttpResponseBody httpResponseBody, PreExecutionResult preExecutionResult);
}
