package com.yolt.providers.common.pis.paymentexecutioncontext.adapter.sepa.initiate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yolt.providers.common.pis.paymentexecutioncontext.CommonPaymentExecutionContext;
import com.yolt.providers.common.pis.paymentexecutioncontext.PaymentExecutionResponseBodyValidator;
import com.yolt.providers.common.pis.paymentexecutioncontext.PaymentStatusesExtractor;
import com.yolt.providers.common.pis.paymentexecutioncontext.errorhandler.*;
import com.yolt.providers.common.pis.paymentexecutioncontext.exception.PaymentExecutionTechnicalException;
import com.yolt.providers.common.pis.paymentexecutioncontext.http.PaymentExecutionHttpHeadersProvider;
import com.yolt.providers.common.pis.paymentexecutioncontext.http.PaymentExecutionHttpRequestBodyProvider;
import com.yolt.providers.common.pis.paymentexecutioncontext.http.PaymentHttpRequestInvoker;
import com.yolt.providers.common.pis.paymentexecutioncontext.model.PaymentAuthorizationUrlExtractor;
import com.yolt.providers.common.pis.paymentexecutioncontext.model.PaymentPreExecutionResultMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.time.Clock;

@RequiredArgsConstructor
public class CommonSepaInitiatePaymentExecutionContextAdapterBuilder<PaymentRequest, HttpRequestBody, HttpResponseBody, PreExecutionResult> {

    private final PaymentStatusesExtractor<HttpResponseBody, PreExecutionResult> paymentStatusesExtractor;
    private final PaymentAuthorizationUrlExtractor<HttpResponseBody, PreExecutionResult> authorizationUrlExtractor;
    private final SepaPaymentProviderStateExtractor<HttpResponseBody, PreExecutionResult> providerStateExtractor;
    private final ObjectMapper objectMapper;
    private final Clock clock;
    private final Class<HttpResponseBody> httpResponseBodyClass;

    private PaymentExecutionHttpRequestBodyProvider<PreExecutionResult, HttpRequestBody> httpRequestBodyProvider = preExecutionResult -> null;
    private PaymentExecutionHttpHeadersProvider<PreExecutionResult, HttpRequestBody> httpHeadersProvider = (preExecutionResult, httpRequestBody) -> new HttpHeaders();
    private PaymentHttpRequestInvoker<HttpRequestBody, PreExecutionResult> httpRequestInvoker = (httpEntity, preExecutionResult) -> ResponseEntity.ok().build();
    private PaymentExecutionResponseBodyValidator<HttpResponseBody> responseBodyValidator = (httpResponseBody, rawResponseBody) -> {
    };
    private PaymentPreExecutionResultMapper<PaymentRequest, PreExecutionResult> preExecutionResultMapper = paymentRequest -> null;

    public static <PaymentRequest, HttpRequestBody, HttpResponseBody, PreExecutionResult> CommonSepaInitiatePaymentExecutionContextAdapterBuilder<PaymentRequest, HttpRequestBody, HttpResponseBody, PreExecutionResult> builder(PaymentStatusesExtractor<HttpResponseBody, PreExecutionResult> paymentStatusesExtractor,
                                                                                                                                                                                                                                 PaymentAuthorizationUrlExtractor<HttpResponseBody, PreExecutionResult> authorizationUrlExtractor,
                                                                                                                                                                                                                                 SepaPaymentProviderStateExtractor<HttpResponseBody, PreExecutionResult> providerStateExtractor,
                                                                                                                                                                                                                                 ObjectMapper objectMapper,
                                                                                                                                                                                                                                 Clock clock,
                                                                                                                                                                                                                                 Class<HttpResponseBody> httpResponseBodyClass) {
        return new CommonSepaInitiatePaymentExecutionContextAdapterBuilder<>(
                paymentStatusesExtractor,
                authorizationUrlExtractor,
                providerStateExtractor,
                objectMapper,
                clock,
                httpResponseBodyClass);
    }

    public CommonSepaInitiatePaymentExecutionContextAdapterBuilder<PaymentRequest, HttpRequestBody, HttpResponseBody, PreExecutionResult> withHttpRequestBodyProvider(PaymentExecutionHttpRequestBodyProvider<PreExecutionResult, HttpRequestBody> httpRequestBodyProvider) {
        this.httpRequestBodyProvider = httpRequestBodyProvider;
        return this;
    }

    public CommonSepaInitiatePaymentExecutionContextAdapterBuilder<PaymentRequest, HttpRequestBody, HttpResponseBody, PreExecutionResult> withHttpHeadersProvider(PaymentExecutionHttpHeadersProvider<PreExecutionResult, HttpRequestBody> httpHeadersProvider) {
        this.httpHeadersProvider = httpHeadersProvider;
        return this;
    }

    public CommonSepaInitiatePaymentExecutionContextAdapterBuilder<PaymentRequest, HttpRequestBody, HttpResponseBody, PreExecutionResult> withHttpRequestInvoker(PaymentHttpRequestInvoker<HttpRequestBody, PreExecutionResult> httpRequestInvoker) {
        this.httpRequestInvoker = httpRequestInvoker;
        return this;
    }

    public CommonSepaInitiatePaymentExecutionContextAdapterBuilder<PaymentRequest, HttpRequestBody, HttpResponseBody, PreExecutionResult> withResponseBodyValidator(PaymentExecutionResponseBodyValidator<HttpResponseBody> responseBodyValidator) {
        this.responseBodyValidator = responseBodyValidator;
        return this;
    }

    public CommonSepaInitiatePaymentExecutionContextAdapterBuilder<PaymentRequest, HttpRequestBody, HttpResponseBody, PreExecutionResult> withPreExecutionResultMapper(PaymentPreExecutionResultMapper<PaymentRequest, PreExecutionResult> preExecutionResultMapper) {
        this.preExecutionResultMapper = preExecutionResultMapper;
        return this;
    }

    public CommonSepaInitiatePaymentExecutionContextAdapter<PaymentRequest, HttpRequestBody, HttpResponseBody, PreExecutionResult> build(RawBankPaymentStatusMapper rawBankPaymentStatusMapper) {
        return buildWithCustomErrorHandlers(new DefaultPaymentExecutionContextPreExecutionErrorHandler(PaymentExecutionTechnicalException::paymentInitiationException),
                new DefaultInitiatePaymentExecutionContextErrorHandler(
                        rawBankPaymentStatusMapper,
                        this.objectMapper,
                        this.clock
                ));
    }

    public CommonSepaInitiatePaymentExecutionContextAdapter<PaymentRequest, HttpRequestBody, HttpResponseBody, PreExecutionResult> buildWithCustomErrorHandler(PaymentExecutionContextErrorHandler customErrorHandler) {
        return buildWithCustomErrorHandlers(new DefaultPaymentExecutionContextPreExecutionErrorHandler(PaymentExecutionTechnicalException::paymentInitiationException),
                customErrorHandler);
    }

    public CommonSepaInitiatePaymentExecutionContextAdapter<PaymentRequest, HttpRequestBody, HttpResponseBody, PreExecutionResult> buildWithCustomPreExecutionErrorHandler(PaymentExecutionContextPreExecutionErrorHandler customPreExecutionErrorHandler,
                                                                                                                                                                           RawBankPaymentStatusMapper rawBankPaymentStatusMapper) {
        return buildWithCustomErrorHandlers(customPreExecutionErrorHandler,
                new DefaultInitiatePaymentExecutionContextErrorHandler(
                        rawBankPaymentStatusMapper,
                        this.objectMapper,
                        this.clock
                ));
    }

    public CommonSepaInitiatePaymentExecutionContextAdapter<PaymentRequest, HttpRequestBody, HttpResponseBody, PreExecutionResult> buildWithCustomErrorHandlers(PaymentExecutionContextPreExecutionErrorHandler customPreExecutionErrorHandler,
                                                                                                                                                                PaymentExecutionContextErrorHandler customErrorHandler) {
        return new CommonSepaInitiatePaymentExecutionContextAdapter<>(
                new CommonPaymentExecutionContext<>(
                        this.httpRequestBodyProvider,
                        this.httpHeadersProvider,
                        this.httpRequestInvoker,
                        this.responseBodyValidator,
                        this.paymentStatusesExtractor,
                        customPreExecutionErrorHandler,
                        customErrorHandler,
                        this.httpResponseBodyClass,
                        this.objectMapper,
                        this.clock
                ),
                this.preExecutionResultMapper,
                this.authorizationUrlExtractor,
                this.providerStateExtractor
        );
    }
}
