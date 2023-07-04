package com.yolt.providers.common.pis.paymentexecutioncontext.model;

@FunctionalInterface
public interface PaymentPreExecutionResultMapper<PaymentRequest, PreExecutionResult> {

    PreExecutionResult map(PaymentRequest paymentRequest);
}
