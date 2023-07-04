package com.yolt.providers.common.pis.paymentexecutioncontext.http;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

@FunctionalInterface
public interface PaymentHttpRequestInvoker<HttpRequestBody, PreExecutionResult> {

    ResponseEntity<JsonNode> invokeRequest(HttpEntity<HttpRequestBody> httpEntity, PreExecutionResult preExecutionResult);
}
