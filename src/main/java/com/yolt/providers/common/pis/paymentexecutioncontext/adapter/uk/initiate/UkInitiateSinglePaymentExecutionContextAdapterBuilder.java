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
import com.yolt.providers.common.pis.ukdomestic.InitiateUkDomesticPaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.time.Clock;

@RequiredArgsConstructor
public class UkInitiateSinglePaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> {

    private final PaymentStatusesExtractor<HttpResponseBody, PreExecutionResult> statusesExtractor;
    private final PaymentAuthorizationUrlExtractor<HttpResponseBody, PreExecutionResult> authorizationUrlExtractor;
    private final UkPaymentProviderStateExtractor<HttpResponseBody, PreExecutionResult> ukPaymentProviderStateExtractor;
    private final ObjectMapper objectMapper;
    private final Clock clock;
    private final Class<HttpResponseBody> httpResponseBodyClass;
    private final CommonUkInitiatePaymentExecutionContextAdapterBuilder<InitiateUkDomesticPaymentRequest, HttpRequestBody, HttpResponseBody, PreExecutionResult> commonBuilder;

    private PaymentExecutionHttpRequestBodyProvider<PreExecutionResult, HttpRequestBody> httpRequestBodyProvider = preExecutionResult -> null;
    private PaymentExecutionHttpHeadersProvider<PreExecutionResult, HttpRequestBody> httpHeadersProvider = (preExecutionResult, httpRequestBody) -> new HttpHeaders();
    private PaymentHttpRequestInvoker<HttpRequestBody, PreExecutionResult> httpRequestInvoker = (httpEntity, preExecutionResult) -> ResponseEntity.ok().build();
    private PaymentExecutionResponseBodyValidator<HttpResponseBody> responseBodyValidator = (httpResponseBody, rawResponseBody) -> {
    };

    public static <HttpRequestBody, HttpResponseBody, PreExecutionResult> UkInitiateSinglePaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> builder(PaymentStatusesExtractor<HttpResponseBody, PreExecutionResult> paymentStatusesExtractor,
                                                                                                                                                                                               PaymentAuthorizationUrlExtractor<HttpResponseBody, PreExecutionResult> authorizationUrlExtractor,
                                                                                                                                                                                               UkPaymentProviderStateExtractor<HttpResponseBody, PreExecutionResult> ukPaymentProviderStateExtractor,
                                                                                                                                                                                               ObjectMapper objectMapper,
                                                                                                                                                                                               Clock clock,
                                                                                                                                                                                               Class<HttpResponseBody> httpResponseBodyClass) {
        return new UkInitiateSinglePaymentExecutionContextAdapterBuilder<>(
                paymentStatusesExtractor,
                authorizationUrlExtractor,
                ukPaymentProviderStateExtractor,
                objectMapper,
                clock,
                httpResponseBodyClass,
                CommonUkInitiatePaymentExecutionContextAdapterBuilder.builder(paymentStatusesExtractor,
                        authorizationUrlExtractor,
                        ukPaymentProviderStateExtractor,
                        objectMapper,
                        clock,
                        httpResponseBodyClass));
    }

    public UkInitiateSinglePaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> withHttpRequestBodyProvider(PaymentExecutionHttpRequestBodyProvider<PreExecutionResult, HttpRequestBody> httpRequestBodyProvider) {
        this.httpRequestBodyProvider = httpRequestBodyProvider;
        this.commonBuilder.withHttpRequestBodyProvider(httpRequestBodyProvider);
        return this;
    }

    public UkInitiateSinglePaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> withHttpHeadersProvider(PaymentExecutionHttpHeadersProvider<PreExecutionResult, HttpRequestBody> httpHeadersProvider) {
        this.httpHeadersProvider = httpHeadersProvider;
        this.commonBuilder.withHttpHeadersProvider(httpHeadersProvider);
        return this;
    }

    public UkInitiateSinglePaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> withHttpRequestInvoker(PaymentHttpRequestInvoker<HttpRequestBody, PreExecutionResult> httpRequestInvoker) {
        this.httpRequestInvoker = httpRequestInvoker;
        this.commonBuilder.withHttpRequestInvoker(httpRequestInvoker);
        return this;
    }

    public UkInitiateSinglePaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> withResponseBodyValidator(PaymentExecutionResponseBodyValidator<HttpResponseBody> responseBodyValidator) {
        this.responseBodyValidator = responseBodyValidator;
        this.commonBuilder.withResponseBodyValidator(responseBodyValidator);
        return this;
    }

    public UkInitiateSinglePaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> withUkDomesticInitiatePaymentPreExecutionResultMapper(UkInitiateSinglePaymentPreExecutionResultMapper<PreExecutionResult> ukDomesticInitiatePaymentPreExecutionResultMapper) {
        this.commonBuilder.withPreExecutionResultMapper(ukDomesticInitiatePaymentPreExecutionResultMapper);
        return this;
    }

    public UkInitiateSinglePaymentExecutionContextAdapter<HttpRequestBody, HttpResponseBody, PreExecutionResult> build(RawBankPaymentStatusMapper rawBankPaymentStatusMapper) {
        return build(new DefaultPaymentExecutionContextPreExecutionErrorHandler(PaymentExecutionTechnicalException::paymentInitiationException),
                new DefaultInitiatePaymentExecutionContextErrorHandler(
                        rawBankPaymentStatusMapper,
                        this.objectMapper,
                        this.clock
                ),
                this.commonBuilder.build(rawBankPaymentStatusMapper));
    }

    public UkInitiateSinglePaymentExecutionContextAdapter<HttpRequestBody, HttpResponseBody, PreExecutionResult> buildWithCustomErrorHandler(PaymentExecutionContextErrorHandler customErrorHandler) {
        return build(new DefaultPaymentExecutionContextPreExecutionErrorHandler(PaymentExecutionTechnicalException::paymentInitiationException),
                customErrorHandler,
                this.commonBuilder.buildWithCustomErrorHandler(customErrorHandler));
    }

    public UkInitiateSinglePaymentExecutionContextAdapter<HttpRequestBody, HttpResponseBody, PreExecutionResult> buildWithCustomPreExecutionErrorHandler(PaymentExecutionContextPreExecutionErrorHandler customPreExecutionErrorHandler,
                                                                                                                                                         RawBankPaymentStatusMapper rawBankPaymentStatusMapper) {
        return build(customPreExecutionErrorHandler,
                new DefaultInitiatePaymentExecutionContextErrorHandler(
                        rawBankPaymentStatusMapper,
                        this.objectMapper,
                        this.clock
                ),
                this.commonBuilder.buildWithCustomPreExecutionErrorHandler(customPreExecutionErrorHandler, rawBankPaymentStatusMapper));
    }

    public UkInitiateSinglePaymentExecutionContextAdapter<HttpRequestBody, HttpResponseBody, PreExecutionResult> buildWithCustomErrorHandlers(PaymentExecutionContextPreExecutionErrorHandler customPreExecutionErrorHandler,
                                                                                                                                              PaymentExecutionContextErrorHandler customErrorHandler) {
        return build(customPreExecutionErrorHandler,
                customErrorHandler,
                this.commonBuilder.buildWithCustomErrorHandlers(customPreExecutionErrorHandler, customErrorHandler));
    }

    private UkInitiateSinglePaymentExecutionContextAdapter<HttpRequestBody, HttpResponseBody, PreExecutionResult> build(PaymentExecutionContextPreExecutionErrorHandler customPreExecutionErrorHandler,
                                                                                                                        PaymentExecutionContextErrorHandler customErrorHandler,
                                                                                                                        CommonUkInitiatePaymentExecutionContextAdapter<InitiateUkDomesticPaymentRequest, HttpRequestBody, HttpResponseBody, PreExecutionResult> commonUkInitiatePaymentExecutionContextAdapter) {
        return new UkInitiateSinglePaymentExecutionContextAdapter<
                >(
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
                this.authorizationUrlExtractor,
                this.ukPaymentProviderStateExtractor,
                commonUkInitiatePaymentExecutionContextAdapter
        );
    }
}
