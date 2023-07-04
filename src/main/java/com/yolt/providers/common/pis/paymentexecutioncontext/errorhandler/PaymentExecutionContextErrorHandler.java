package com.yolt.providers.common.pis.paymentexecutioncontext.errorhandler;

import com.yolt.providers.common.pis.paymentexecutioncontext.model.PaymentExecutionResult;
import org.springframework.http.HttpEntity;

import java.time.Instant;

public interface PaymentExecutionContextErrorHandler {

    <HttpRequestBody, HttpResponseBody, PreExecutionResult> PaymentExecutionResult<HttpResponseBody, PreExecutionResult> handleError(HttpEntity<HttpRequestBody> httpEntity, Instant requestTimestamp, Exception ex);
}
