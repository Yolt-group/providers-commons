package com.yolt.providers.common.pis.paymentexecutioncontext.adapter.uk.initiate;

import com.yolt.providers.common.pis.ukdomestic.UkProviderState;

@FunctionalInterface
public interface UkPaymentProviderStateExtractor<HttpResponseBody, PreExecutionResult> {

    UkProviderState extractUkProviderState(HttpResponseBody httpResponseBody, PreExecutionResult preExecutionResult);
}
