package com.yolt.providers.common.pis.paymentexecutioncontext.adapter.uk.submit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yolt.providers.common.pis.paymentexecutioncontext.CommonPaymentExecutionContext;
import com.yolt.providers.common.pis.paymentexecutioncontext.PaymentExecutionResponseBodyValidator;
import com.yolt.providers.common.pis.paymentexecutioncontext.PaymentStatusesExtractor;
import com.yolt.providers.common.pis.paymentexecutioncontext.adapter.uk.initiate.UkPaymentProviderStateExtractor;
import com.yolt.providers.common.pis.paymentexecutioncontext.errorhandler.*;
import com.yolt.providers.common.pis.paymentexecutioncontext.exception.PaymentExecutionTechnicalException;
import com.yolt.providers.common.pis.paymentexecutioncontext.http.PaymentExecutionHttpHeadersProvider;
import com.yolt.providers.common.pis.paymentexecutioncontext.http.PaymentExecutionHttpRequestBodyProvider;
import com.yolt.providers.common.pis.paymentexecutioncontext.http.PaymentHttpRequestInvoker;
import com.yolt.providers.common.pis.paymentexecutioncontext.model.PaymentIdExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.time.Clock;

@RequiredArgsConstructor
public class UkSubmitPaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> {

    private final PaymentStatusesExtractor<HttpResponseBody, PreExecutionResult> statusesExtractor;
    private final PaymentIdExtractor<HttpResponseBody, PreExecutionResult> paymentIdExtractor;
    private final UkPaymentProviderStateExtractor<HttpResponseBody, PreExecutionResult> ukPaymentProviderStateExtractor;
    private final ObjectMapper objectMapper;
    private final Clock clock;
    private final Class<HttpResponseBody> httpResponseBodyClass;

    private PaymentExecutionHttpRequestBodyProvider<PreExecutionResult, HttpRequestBody> httpRequestBodyProvider = preExecutionResult -> null;
    private PaymentExecutionHttpHeadersProvider<PreExecutionResult, HttpRequestBody> httpHeadersProvider = (preExecutionResult, httpRequestBody) -> new HttpHeaders();
    private PaymentHttpRequestInvoker<HttpRequestBody, PreExecutionResult> httpRequestInvoker = (httpEntity, preExecutionResult) -> ResponseEntity.ok().build();
    private PaymentExecutionResponseBodyValidator<HttpResponseBody> responseBodyValidator = (httpResponseBody, rawResponseBody) -> {
    };
    private UkDomesticSubmitPreExecutionResultMapper<PreExecutionResult> ukDomesticSubmitPreExecutionResultMapper = submitPaymentRequest -> null;


    public static <HttpRequestBody, HttpResponseBody, PreExecutionResult> UkSubmitPaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> builder(PaymentStatusesExtractor<HttpResponseBody, PreExecutionResult> paymentStatusesExtractor,
                                                                                                                                                                                       PaymentIdExtractor<HttpResponseBody, PreExecutionResult> paymentIdExtractor,
                                                                                                                                                                                       UkPaymentProviderStateExtractor<HttpResponseBody, PreExecutionResult> ukPaymentProviderStateExtractor,
                                                                                                                                                                                       ObjectMapper objectMapper,
                                                                                                                                                                                       Clock clock,
                                                                                                                                                                                       Class<HttpResponseBody> httpResponseBodyClass) {
        return new UkSubmitPaymentExecutionContextAdapterBuilder<>(
                paymentStatusesExtractor,
                paymentIdExtractor,
                ukPaymentProviderStateExtractor,
                objectMapper,
                clock,
                httpResponseBodyClass
        );
    }

    public UkSubmitPaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> withHttpRequestBodyProvider(PaymentExecutionHttpRequestBodyProvider<PreExecutionResult, HttpRequestBody> httpRequestBodyProvider) {
        this.httpRequestBodyProvider = httpRequestBodyProvider;
        return this;
    }

    public UkSubmitPaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> withHttpHeadersProvider(PaymentExecutionHttpHeadersProvider<PreExecutionResult, HttpRequestBody> httpHeadersProvider) {
        this.httpHeadersProvider = httpHeadersProvider;
        return this;
    }

    public UkSubmitPaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> withHttpRequestInvoker(PaymentHttpRequestInvoker<HttpRequestBody, PreExecutionResult> httpRequestInvoker) {
        this.httpRequestInvoker = httpRequestInvoker;
        return this;
    }

    public UkSubmitPaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> withResponseBodyValidator(PaymentExecutionResponseBodyValidator<HttpResponseBody> responseBodyValidator) {
        this.responseBodyValidator = responseBodyValidator;
        return this;
    }

    public UkSubmitPaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> withUkDomesticSubmitPreExecutionResultMapper(UkDomesticSubmitPreExecutionResultMapper<PreExecutionResult> ukDomesticSubmitPreExecutionResultMapper) {
        this.ukDomesticSubmitPreExecutionResultMapper = ukDomesticSubmitPreExecutionResultMapper;
        return this;
    }

    public UkSubmitPaymentExecutionContextAdapter<HttpRequestBody, HttpResponseBody, PreExecutionResult> build(RawBankPaymentStatusMapper rawBankPaymentStatusMapper) {
        return buildWithCustomErrorHandlers(new DefaultPaymentExecutionContextPreExecutionErrorHandler(PaymentExecutionTechnicalException::paymentSubmissionException),
                new DefaultSubmitPaymentExecutionContextErrorHandler(
                        rawBankPaymentStatusMapper,
                        this.objectMapper,
                        this.clock
                ));
    }

    public UkSubmitPaymentExecutionContextAdapter<HttpRequestBody, HttpResponseBody, PreExecutionResult> buildWithCustomErrorHandler(PaymentExecutionContextErrorHandler customErrorHandler) {
        return buildWithCustomErrorHandlers(new DefaultPaymentExecutionContextPreExecutionErrorHandler(PaymentExecutionTechnicalException::paymentSubmissionException),
                customErrorHandler);
    }

    public UkSubmitPaymentExecutionContextAdapter<HttpRequestBody, HttpResponseBody, PreExecutionResult> buildWithCustomPreExecutionErrorHandler(PaymentExecutionContextPreExecutionErrorHandler customPreExecutionErrorHandler,
                                                                                                                                                 RawBankPaymentStatusMapper rawBankPaymentStatusMapper) {
        return buildWithCustomErrorHandlers(customPreExecutionErrorHandler,
                new DefaultSubmitPaymentExecutionContextErrorHandler(
                        rawBankPaymentStatusMapper,
                        this.objectMapper,
                        this.clock
                ));
    }

    public UkSubmitPaymentExecutionContextAdapter<HttpRequestBody, HttpResponseBody, PreExecutionResult> buildWithCustomErrorHandlers(PaymentExecutionContextPreExecutionErrorHandler customPreExecutionErrorHandler,
                                                                                                                                      PaymentExecutionContextErrorHandler customErrorHandler) {
        return new UkSubmitPaymentExecutionContextAdapter<>(
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
                this.ukDomesticSubmitPreExecutionResultMapper,
                this.paymentIdExtractor,
                this.ukPaymentProviderStateExtractor,
                this.objectMapper
        );
    }
}
