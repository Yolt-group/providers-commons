package com.yolt.providers.common.pis.paymentexecutioncontext.adapter.sepa.initiate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yolt.providers.common.pis.paymentexecutioncontext.PaymentExecutionResponseBodyValidator;
import com.yolt.providers.common.pis.paymentexecutioncontext.PaymentStatusesExtractor;
import com.yolt.providers.common.pis.paymentexecutioncontext.errorhandler.PaymentExecutionContextErrorHandler;
import com.yolt.providers.common.pis.paymentexecutioncontext.errorhandler.PaymentExecutionContextPreExecutionErrorHandler;
import com.yolt.providers.common.pis.paymentexecutioncontext.errorhandler.RawBankPaymentStatusMapper;
import com.yolt.providers.common.pis.paymentexecutioncontext.http.PaymentExecutionHttpHeadersProvider;
import com.yolt.providers.common.pis.paymentexecutioncontext.http.PaymentExecutionHttpRequestBodyProvider;
import com.yolt.providers.common.pis.paymentexecutioncontext.http.PaymentHttpRequestInvoker;
import com.yolt.providers.common.pis.paymentexecutioncontext.model.PaymentAuthorizationUrlExtractor;
import com.yolt.providers.common.pis.sepa.InitiatePaymentRequest;
import lombok.RequiredArgsConstructor;

import java.time.Clock;

@RequiredArgsConstructor
public class SepaInitiatePeriodicPaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> {

    private final CommonSepaInitiatePaymentExecutionContextAdapterBuilder<InitiatePaymentRequest, HttpRequestBody, HttpResponseBody, PreExecutionResult> commonBuilder;

    public static <HttpRequestBody, HttpResponseBody, PreExecutionResult> SepaInitiatePeriodicPaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> builder(PaymentStatusesExtractor<HttpResponseBody, PreExecutionResult> paymentStatusesExtractor,
                                                                                                                                                                                                   PaymentAuthorizationUrlExtractor<HttpResponseBody, PreExecutionResult> authorizationUrlExtractor,
                                                                                                                                                                                                   SepaPaymentProviderStateExtractor<HttpResponseBody, PreExecutionResult> providerStateExtractor,
                                                                                                                                                                                                   ObjectMapper objectMapper,
                                                                                                                                                                                                   Clock clock,
                                                                                                                                                                                                   Class<HttpResponseBody> httpResponseBodyClass) {
        return new SepaInitiatePeriodicPaymentExecutionContextAdapterBuilder<>(
                CommonSepaInitiatePaymentExecutionContextAdapterBuilder.builder(paymentStatusesExtractor,
                        authorizationUrlExtractor,
                        providerStateExtractor,
                        objectMapper,
                        clock,
                        httpResponseBodyClass));
    }

    public SepaInitiatePeriodicPaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> withHttpRequestBodyProvider(PaymentExecutionHttpRequestBodyProvider<PreExecutionResult, HttpRequestBody> httpRequestBodyProvider) {
        this.commonBuilder.withHttpRequestBodyProvider(httpRequestBodyProvider);
        return this;
    }

    public SepaInitiatePeriodicPaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> withHttpHeadersProvider(PaymentExecutionHttpHeadersProvider<PreExecutionResult, HttpRequestBody> httpHeadersProvider) {
        this.commonBuilder.withHttpHeadersProvider(httpHeadersProvider);
        return this;
    }

    public SepaInitiatePeriodicPaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> withHttpRequestInvoker(PaymentHttpRequestInvoker<HttpRequestBody, PreExecutionResult> httpRequestInvoker) {
        this.commonBuilder.withHttpRequestInvoker(httpRequestInvoker);
        return this;
    }

    public SepaInitiatePeriodicPaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> withResponseBodyValidator(PaymentExecutionResponseBodyValidator<HttpResponseBody> responseBodyValidator) {
        this.commonBuilder.withResponseBodyValidator(responseBodyValidator);
        return this;
    }

    public SepaInitiatePeriodicPaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> withPreExecutionResultMapper(SepaInitiatePeriodicPaymentPreExecutionResultMapper<PreExecutionResult> preExecutionResultMapper) {
        this.commonBuilder.withPreExecutionResultMapper(preExecutionResultMapper);
        return this;
    }

    public SepaInitiatePeriodicPaymentExecutionContextAdapter<HttpRequestBody, HttpResponseBody, PreExecutionResult> build(RawBankPaymentStatusMapper rawBankPaymentStatusMapper) {
        return new SepaInitiatePeriodicPaymentExecutionContextAdapter<>(this.commonBuilder.build(rawBankPaymentStatusMapper));
    }

    public SepaInitiatePeriodicPaymentExecutionContextAdapter<HttpRequestBody, HttpResponseBody, PreExecutionResult> buildWithCustomErrorHandler(PaymentExecutionContextErrorHandler customErrorHandler) {
        return new SepaInitiatePeriodicPaymentExecutionContextAdapter<>(this.commonBuilder.buildWithCustomErrorHandler(customErrorHandler));
    }

    public SepaInitiatePeriodicPaymentExecutionContextAdapter<HttpRequestBody, HttpResponseBody, PreExecutionResult> buildWithCustomPreExecutionErrorHandler(PaymentExecutionContextPreExecutionErrorHandler customPreExecutionErrorHandler,
                                                                                                                                                             RawBankPaymentStatusMapper rawBankPaymentStatusMapper) {
        return new SepaInitiatePeriodicPaymentExecutionContextAdapter<>(this.commonBuilder.buildWithCustomPreExecutionErrorHandler(customPreExecutionErrorHandler, rawBankPaymentStatusMapper));
    }

    public SepaInitiatePeriodicPaymentExecutionContextAdapter<HttpRequestBody, HttpResponseBody, PreExecutionResult> buildWithCustomErrorHandlers(PaymentExecutionContextPreExecutionErrorHandler customPreExecutionErrorHandler,
                                                                                                                                                  PaymentExecutionContextErrorHandler customErrorHandler) {
        return new SepaInitiatePeriodicPaymentExecutionContextAdapter<>(this.commonBuilder.buildWithCustomErrorHandlers(customPreExecutionErrorHandler, customErrorHandler));
    }
}
