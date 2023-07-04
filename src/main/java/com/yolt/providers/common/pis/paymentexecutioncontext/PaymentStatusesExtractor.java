package com.yolt.providers.common.pis.paymentexecutioncontext;

import com.yolt.providers.common.pis.paymentexecutioncontext.model.PaymentStatuses;

public interface PaymentStatusesExtractor<HttpResponseBody, PreExecutionResult> {

    PaymentStatuses extractPaymentStatuses(HttpResponseBody httpResponseBody, PreExecutionResult preExecutionResult);
}
