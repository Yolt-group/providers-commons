package com.yolt.providers.common.pis.paymentexecutioncontext.adapter.uk.initiate;

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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.time.Clock;

/**
 * @deprecated Use {@link UkInitiateSinglePaymentExecutionContextAdapterBuilder} instead
 * @param <HttpRequestBody>
 * @param <HttpResponseBody>
 * @param <PreExecutionResult>
 */
@Deprecated
@RequiredArgsConstructor
public class UkInitiatePaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> {

    private final PaymentStatusesExtractor<HttpResponseBody, PreExecutionResult> statusesExtractor;
    private final PaymentAuthorizationUrlExtractor<HttpResponseBody, PreExecutionResult> authorizationUrlExtractor;
    private final UkPaymentProviderStateExtractor<HttpResponseBody, PreExecutionResult> ukPaymentProviderStateExtractor;
    private final ObjectMapper objectMapper;
    private final Clock clock;
    private final Class<HttpResponseBody> httpResponseBodyClass;

    private PaymentExecutionHttpRequestBodyProvider<PreExecutionResult, HttpRequestBody> httpRequestBodyProvider = preExecutionResult -> null;
    private PaymentExecutionHttpHeadersProvider<PreExecutionResult, HttpRequestBody> httpHeadersProvider = (preExecutionResult, httpRequestBody) -> new HttpHeaders();
    private PaymentHttpRequestInvoker<HttpRequestBody, PreExecutionResult> httpRequestInvoker = (httpEntity, preExecutionResult) -> ResponseEntity.ok().build();
    private PaymentExecutionResponseBodyValidator<HttpResponseBody> responseBodyValidator = (httpResponseBody, rawResponseBody) -> {
    };
    private UkDomesticInitiatePaymentPreExecutionResultMapper<PreExecutionResult> ukDomesticInitiatePaymentPreExecutionResultMapper = initiateUkDomesticPaymentRequest -> null;


    public static <HttpRequestBody, HttpResponseBody, PreExecutionResult> UkInitiatePaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> builder(PaymentStatusesExtractor<HttpResponseBody, PreExecutionResult> paymentStatusesExtractor,
                                                                                                                                                                                         PaymentAuthorizationUrlExtractor<HttpResponseBody, PreExecutionResult> authorizationUrlExtractor,
                                                                                                                                                                                         UkPaymentProviderStateExtractor<HttpResponseBody, PreExecutionResult> ukPaymentProviderStateExtractor,
                                                                                                                                                                                         ObjectMapper objectMapper,
                                                                                                                                                                                         Clock clock,
                                                                                                                                                                                         Class<HttpResponseBody> httpResponseBodyClass) {
        return new UkInitiatePaymentExecutionContextAdapterBuilder<>(
                paymentStatusesExtractor,
                authorizationUrlExtractor,
                ukPaymentProviderStateExtractor,
                objectMapper,
                clock,
                httpResponseBodyClass);
    }

    public UkInitiatePaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> withHttpRequestBodyProvider(PaymentExecutionHttpRequestBodyProvider<PreExecutionResult, HttpRequestBody> httpRequestBodyProvider) {
        this.httpRequestBodyProvider = httpRequestBodyProvider;
        return this;
    }

    public UkInitiatePaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> withHttpHeadersProvider(PaymentExecutionHttpHeadersProvider<PreExecutionResult, HttpRequestBody> httpHeadersProvider) {
        this.httpHeadersProvider = httpHeadersProvider;
        return this;
    }

    public UkInitiatePaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> withHttpRequestInvoker(PaymentHttpRequestInvoker<HttpRequestBody, PreExecutionResult> httpRequestInvoker) {
        this.httpRequestInvoker = httpRequestInvoker;
        return this;
    }

    public UkInitiatePaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> withResponseBodyValidator(PaymentExecutionResponseBodyValidator<HttpResponseBody> responseBodyValidator) {
        this.responseBodyValidator = responseBodyValidator;
        return this;
    }

    public UkInitiatePaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> withUkDomesticInitiatePaymentPreExecutionResultMapper(UkDomesticInitiatePaymentPreExecutionResultMapper<PreExecutionResult> ukDomesticInitiatePaymentPreExecutionResultMapper) {
        this.ukDomesticInitiatePaymentPreExecutionResultMapper = ukDomesticInitiatePaymentPreExecutionResultMapper;
        return this;
    }

    public UkInitiatePaymentExecutionContextAdapter<HttpRequestBody, HttpResponseBody, PreExecutionResult> build(RawBankPaymentStatusMapper rawBankPaymentStatusMapper) {
        return buildWithCustomErrorHandlers(new DefaultPaymentExecutionContextPreExecutionErrorHandler(PaymentExecutionTechnicalException::paymentInitiationException),
                new DefaultInitiatePaymentExecutionContextErrorHandler(
                        rawBankPaymentStatusMapper,
                        this.objectMapper,
                        this.clock
                ));
    }

    public UkInitiatePaymentExecutionContextAdapter<HttpRequestBody, HttpResponseBody, PreExecutionResult> buildWithCustomErrorHandler(PaymentExecutionContextErrorHandler customErrorHandler) {
        return buildWithCustomErrorHandlers(new DefaultPaymentExecutionContextPreExecutionErrorHandler(PaymentExecutionTechnicalException::paymentInitiationException),
                customErrorHandler);
    }

    public UkInitiatePaymentExecutionContextAdapter<HttpRequestBody, HttpResponseBody, PreExecutionResult> buildWithCustomPreExecutionErrorHandler(PaymentExecutionContextPreExecutionErrorHandler customPreExecutionErrorHandler,
                                                                                                                                                   RawBankPaymentStatusMapper rawBankPaymentStatusMapper) {
        return buildWithCustomErrorHandlers(customPreExecutionErrorHandler,
                new DefaultInitiatePaymentExecutionContextErrorHandler(
                        rawBankPaymentStatusMapper,
                        this.objectMapper,
                        this.clock
                ));
    }

    public UkInitiatePaymentExecutionContextAdapter<HttpRequestBody, HttpResponseBody, PreExecutionResult> buildWithCustomErrorHandlers(PaymentExecutionContextPreExecutionErrorHandler customPreExecutionErrorHandler,
                                                                                                                                        PaymentExecutionContextErrorHandler customErrorHandler) {
        return new UkInitiatePaymentExecutionContextAdapter<>(
                new CommonPaymentExecutionContext<>(
                        this.httpRequestBodyProvider,
                        this.httpHeadersProvider,
                        this.httpRequestInvoker,
                        this.responseBodyValidator,
                        this.statusesExtractor,
                        customPreExecutionErrorHandler,
                        customErrorHandler,
                        this.httpResponseBodyClass,
                        this.objectMapper,
                        this.clock
                ),
                this.ukDomesticInitiatePaymentPreExecutionResultMapper,
                this.authorizationUrlExtractor,
                this.ukPaymentProviderStateExtractor,
                this.objectMapper
        );
    }
}
