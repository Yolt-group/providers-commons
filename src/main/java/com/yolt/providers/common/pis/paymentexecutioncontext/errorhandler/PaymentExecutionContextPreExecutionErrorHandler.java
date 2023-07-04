package com.yolt.providers.common.pis.paymentexecutioncontext.errorhandler;

import com.yolt.providers.common.pis.paymentexecutioncontext.model.PaymentExecutionResult;

public interface PaymentExecutionContextPreExecutionErrorHandler {

    <HttpResponseBody, PreExecutionResult> PaymentExecutionResult<HttpResponseBody, PreExecutionResult> handleError(Exception ex);
}
