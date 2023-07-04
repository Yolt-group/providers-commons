package com.yolt.providers.common.pis.paymentexecutioncontext;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yolt.providers.common.exception.PaymentValidationException;
import com.yolt.providers.common.pis.paymentexecutioncontext.errorhandler.PaymentExecutionContextErrorHandler;
import com.yolt.providers.common.pis.paymentexecutioncontext.errorhandler.PaymentExecutionContextPreExecutionErrorHandler;
import com.yolt.providers.common.pis.paymentexecutioncontext.http.PaymentExecutionHttpHeadersProvider;
import com.yolt.providers.common.pis.paymentexecutioncontext.http.PaymentExecutionHttpRequestBodyProvider;
import com.yolt.providers.common.pis.paymentexecutioncontext.http.PaymentHttpRequestInvoker;
import com.yolt.providers.common.pis.paymentexecutioncontext.model.PaymentExecutionResult;
import com.yolt.providers.common.pis.paymentexecutioncontext.model.PaymentPreExecutionResultProvider;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.time.Clock;
import java.time.Instant;

public class CommonPaymentExecutionContext<HttpRequestBody, HttpResponseBody, PreExecutionResult> {

    private final PaymentExecutionHttpRequestBodyProvider<PreExecutionResult, HttpRequestBody> httpRequestBodyProvider;
    private final PaymentExecutionHttpHeadersProvider<PreExecutionResult, HttpRequestBody> httpHeadersProvider;
    private final PaymentHttpRequestInvoker<HttpRequestBody, PreExecutionResult> paymentHttpRequestInvoker;
    private final PaymentExecutionResponseBodyValidator<HttpResponseBody> paymentExecutionResponseBodyValidator;
    private final PaymentStatusesExtractor<HttpResponseBody, PreExecutionResult> paymentStatusesExtractor;
    private final PaymentExecutionContextPreExecutionErrorHandler preExecutionErrorHandler;
    private final PaymentExecutionContextErrorHandler paymentExecutionContextErrorHandler;
    private final Class<HttpResponseBody> httpResponseBodyClass;
    private final ObjectMapper objectMapper;
    private final Clock clock;

    public CommonPaymentExecutionContext(
            PaymentExecutionHttpRequestBodyProvider<PreExecutionResult, HttpRequestBody> httpRequestBodyProvider,
            PaymentExecutionHttpHeadersProvider<PreExecutionResult, HttpRequestBody> httpHeadersProvider,
            PaymentHttpRequestInvoker<HttpRequestBody, PreExecutionResult> paymentHttpRequestInvoker,
            PaymentExecutionResponseBodyValidator<HttpResponseBody> paymentExecutionResponseBodyValidator,
            PaymentStatusesExtractor<HttpResponseBody, PreExecutionResult> paymentStatusesExtractor,
            PaymentExecutionContextPreExecutionErrorHandler preExecutionErrorHandler,
            PaymentExecutionContextErrorHandler paymentExecutionContextErrorHandler,
            Class<HttpResponseBody> httpResponseBodyClass,
            ObjectMapper objectMapper,
            Clock clock) {
        this.httpRequestBodyProvider = httpRequestBodyProvider;
        this.httpHeadersProvider = httpHeadersProvider;
        this.paymentHttpRequestInvoker = paymentHttpRequestInvoker;
        this.paymentExecutionResponseBodyValidator = paymentExecutionResponseBodyValidator;
        this.paymentStatusesExtractor = paymentStatusesExtractor;
        this.paymentExecutionContextErrorHandler = paymentExecutionContextErrorHandler;
        this.preExecutionErrorHandler = preExecutionErrorHandler;
        this.httpResponseBodyClass = httpResponseBodyClass;
        this.objectMapper = objectMapper
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.clock = clock;
    }

    public PaymentExecutionResult<HttpResponseBody, PreExecutionResult> execute(PaymentPreExecutionResultProvider<PreExecutionResult> preExecutionResultProvider) {
        PreExecutionResult preExecutionResult;
        HttpRequestBody httpRequestBody;
        HttpHeaders httpHeaders;
        try {
            preExecutionResult = preExecutionResultProvider.providePreExecutionResult();
            httpRequestBody = httpRequestBodyProvider.provideHttpRequestBody(preExecutionResult);
            httpHeaders = httpHeadersProvider.provideHttpHeaders(preExecutionResult, httpRequestBody);
        } catch (Exception ex) {
            return preExecutionErrorHandler.handleError(ex);
        }

        HttpEntity<HttpRequestBody> requestEntity = new HttpEntity<>(httpRequestBody, httpHeaders);
        Instant requestTimestamp = Instant.now(clock);
        ResponseEntity<JsonNode> responseEntity;
        Instant responseTimestamp;
        JsonNode rawBody;
        HttpResponseBody httpResponseBody;
        try {
            responseEntity = paymentHttpRequestInvoker.invokeRequest(requestEntity, preExecutionResult);
            responseTimestamp = Instant.now(clock);
            rawBody = responseEntity.getBody();
            httpResponseBody = objectMapper.treeToValue(rawBody, httpResponseBodyClass);
            paymentExecutionResponseBodyValidator.validate(httpResponseBody, rawBody);
        } catch (PaymentValidationException ex) {
            return preExecutionErrorHandler.handleError(ex);
        } catch (Exception ex) {
            return paymentExecutionContextErrorHandler.handleError(requestEntity, requestTimestamp, ex);
        }
        try {
            return new PaymentExecutionResult<>(
                    requestTimestamp,
                    responseTimestamp,
                    objectMapper.writeValueAsString(requestEntity.getBody()),
                    objectMapper.writeValueAsString(rawBody),
                    requestEntity.getHeaders().toString(),
                    responseEntity.getHeaders().toString(),
                    paymentStatusesExtractor.extractPaymentStatuses(httpResponseBody, preExecutionResult),
                    httpResponseBody,
                    preExecutionResult
            );
        } catch (Exception ex) {
            return preExecutionErrorHandler.handleError(ex);
        }
    }
}
