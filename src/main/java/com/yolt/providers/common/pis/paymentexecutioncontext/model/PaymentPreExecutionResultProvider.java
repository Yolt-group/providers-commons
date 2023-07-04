package com.yolt.providers.common.pis.paymentexecutioncontext.model;

import com.yolt.providers.common.pis.paymentexecutioncontext.exception.PaymentExecutionTechnicalException;

@FunctionalInterface
public interface PaymentPreExecutionResultProvider<PreExecutionResult> {

    PreExecutionResult providePreExecutionResult() throws PaymentExecutionTechnicalException;
}
