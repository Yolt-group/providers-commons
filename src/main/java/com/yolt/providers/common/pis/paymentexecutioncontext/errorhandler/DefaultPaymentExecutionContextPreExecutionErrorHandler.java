package com.yolt.providers.common.pis.paymentexecutioncontext.errorhandler;

import com.yolt.providers.common.pis.paymentexecutioncontext.model.PaymentExecutionResult;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultPaymentExecutionContextPreExecutionErrorHandler implements PaymentExecutionContextPreExecutionErrorHandler {

    private final PaymentExecutionContextTechnicalExceptionSupplier paymentExecutionContextTechnicalExceptionSupplier;

    @Override
    public <HttpResponseBody, PreExecutionResult> PaymentExecutionResult<HttpResponseBody, PreExecutionResult> handleError(Exception ex) {
        throw paymentExecutionContextTechnicalExceptionSupplier.createPaymentExecutionTechnicalException(ex);
    }
}
