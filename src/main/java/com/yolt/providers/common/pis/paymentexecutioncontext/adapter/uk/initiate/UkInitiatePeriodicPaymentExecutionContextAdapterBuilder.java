package com.yolt.providers.common.pis.paymentexecutioncontext.adapter.uk.initiate;

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
import com.yolt.providers.common.pis.ukdomestic.InitiateUkDomesticPeriodicPaymentRequest;
import lombok.RequiredArgsConstructor;

import java.time.Clock;

@RequiredArgsConstructor
public class UkInitiatePeriodicPaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> {

    private final CommonUkInitiatePaymentExecutionContextAdapterBuilder<InitiateUkDomesticPeriodicPaymentRequest, HttpRequestBody, HttpResponseBody, PreExecutionResult> commonBuilder;

    public static <HttpRequestBody, HttpResponseBody, PreExecutionResult> UkInitiatePeriodicPaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> builder(PaymentStatusesExtractor<HttpResponseBody, PreExecutionResult> paymentStatusesExtractor,
                                                                                                                                                                                                 PaymentAuthorizationUrlExtractor<HttpResponseBody, PreExecutionResult> authorizationUrlExtractor,
                                                                                                                                                                                                 UkPaymentProviderStateExtractor<HttpResponseBody, PreExecutionResult> ukPaymentProviderStateExtractor,
                                                                                                                                                                                                 ObjectMapper objectMapper,
                                                                                                                                                                                                 Clock clock,
                                                                                                                                                                                                 Class<HttpResponseBody> httpResponseBodyClass) {
        return new UkInitiatePeriodicPaymentExecutionContextAdapterBuilder<>(
                CommonUkInitiatePaymentExecutionContextAdapterBuilder.builder(paymentStatusesExtractor,
                        authorizationUrlExtractor,
                        ukPaymentProviderStateExtractor,
                        objectMapper,
                        clock,
                        httpResponseBodyClass));
    }

    public UkInitiatePeriodicPaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> withHttpRequestBodyProvider(PaymentExecutionHttpRequestBodyProvider<PreExecutionResult, HttpRequestBody> httpRequestBodyProvider) {
        this.commonBuilder.withHttpRequestBodyProvider(httpRequestBodyProvider);
        return this;
    }

    public UkInitiatePeriodicPaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> withHttpHeadersProvider(PaymentExecutionHttpHeadersProvider<PreExecutionResult, HttpRequestBody> httpHeadersProvider) {
        this.commonBuilder.withHttpHeadersProvider(httpHeadersProvider);
        return this;
    }

    public UkInitiatePeriodicPaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> withHttpRequestInvoker(PaymentHttpRequestInvoker<HttpRequestBody, PreExecutionResult> httpRequestInvoker) {
        this.commonBuilder.withHttpRequestInvoker(httpRequestInvoker);
        return this;
    }

    public UkInitiatePeriodicPaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> withResponseBodyValidator(PaymentExecutionResponseBodyValidator<HttpResponseBody> responseBodyValidator) {
        this.commonBuilder.withResponseBodyValidator(responseBodyValidator);
        return this;
    }

    public UkInitiatePeriodicPaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> withPreExecutionResultMapper(UkInitiatePeriodicPaymentPreExecutionResultMapper<PreExecutionResult> preExecutionResultMapper) {
        this.commonBuilder.withPreExecutionResultMapper(preExecutionResultMapper);
        return this;
    }

    public UkInitiatePeriodicPaymentExecutionContextAdapter<HttpRequestBody, HttpResponseBody, PreExecutionResult> build(RawBankPaymentStatusMapper rawBankPaymentStatusMapper) {
        return new UkInitiatePeriodicPaymentExecutionContextAdapter<>(this.commonBuilder.build(rawBankPaymentStatusMapper));
    }

    public UkInitiatePeriodicPaymentExecutionContextAdapter<HttpRequestBody, HttpResponseBody, PreExecutionResult> buildWithCustomErrorHandler(PaymentExecutionContextErrorHandler customErrorHandler) {
        return new UkInitiatePeriodicPaymentExecutionContextAdapter<>(this.commonBuilder.buildWithCustomErrorHandler(customErrorHandler));
    }

    public UkInitiatePeriodicPaymentExecutionContextAdapter<HttpRequestBody, HttpResponseBody, PreExecutionResult> buildWithCustomPreExecutionErrorHandler(PaymentExecutionContextPreExecutionErrorHandler customPreExecutionErrorHandler,
                                                                                                                                                           RawBankPaymentStatusMapper rawBankPaymentStatusMapper) {
        return new UkInitiatePeriodicPaymentExecutionContextAdapter<>(this.commonBuilder.buildWithCustomPreExecutionErrorHandler(customPreExecutionErrorHandler, rawBankPaymentStatusMapper));
    }

    public UkInitiatePeriodicPaymentExecutionContextAdapter<HttpRequestBody, HttpResponseBody, PreExecutionResult> buildWithCustomErrorHandlers(PaymentExecutionContextPreExecutionErrorHandler customPreExecutionErrorHandler,
                                                                                                                                                PaymentExecutionContextErrorHandler customErrorHandler) {
        return new UkInitiatePeriodicPaymentExecutionContextAdapter<>(this.commonBuilder.buildWithCustomErrorHandlers(customPreExecutionErrorHandler, customErrorHandler));
    }
}
