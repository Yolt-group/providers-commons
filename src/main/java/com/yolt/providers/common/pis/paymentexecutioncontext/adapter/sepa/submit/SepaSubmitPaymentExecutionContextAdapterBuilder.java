package com.yolt.providers.common.pis.paymentexecutioncontext.adapter.sepa.submit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yolt.providers.common.pis.paymentexecutioncontext.CommonPaymentExecutionContext;
import com.yolt.providers.common.pis.paymentexecutioncontext.PaymentExecutionResponseBodyValidator;
import com.yolt.providers.common.pis.paymentexecutioncontext.PaymentStatusesExtractor;
import com.yolt.providers.common.pis.paymentexecutioncontext.adapter.sepa.initiate.SepaPaymentProviderStateExtractor;
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
public class SepaSubmitPaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> {

    private final PaymentStatusesExtractor<HttpResponseBody, PreExecutionResult> paymentStatusesExtractor;
    private final PaymentIdExtractor<HttpResponseBody, PreExecutionResult> paymentIdExtractor;
    private final SepaPaymentProviderStateExtractor<HttpResponseBody, PreExecutionResult> providerStateExtractor;
    private final ObjectMapper objectMapper;
    private final Clock clock;
    private final Class<HttpResponseBody> httpResponseBodyClass;

    private PaymentExecutionHttpRequestBodyProvider<PreExecutionResult, HttpRequestBody> httpRequestBodyProvider = preExecutionResult -> null;
    private PaymentExecutionHttpHeadersProvider<PreExecutionResult, HttpRequestBody> httpHeadersProvider = (preExecutionResult, httpRequestBody) -> new HttpHeaders();
    private PaymentHttpRequestInvoker<HttpRequestBody, PreExecutionResult> httpRequestInvoker = (httpEntity, preExecutionResult) -> ResponseEntity.ok().build();
    private PaymentExecutionResponseBodyValidator<HttpResponseBody> responseBodyValidator = (httpResponseBody, rawResponseBody) -> {
    };
    private SepaSubmitPaymentPreExecutionResultMapper<PreExecutionResult> preExecutionResultMapper = initiatePaymentRequest -> null;
    private boolean getStatusAsSubmitStep = false;

    public static <HttpRequestBody, HttpResponseBody, PreExecutionResult> SepaSubmitPaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> builder(PaymentStatusesExtractor<HttpResponseBody, PreExecutionResult> paymentStatusesExtractor,
                                                                                                                                                                                         PaymentIdExtractor<HttpResponseBody, PreExecutionResult> paymentIdExtractor,
                                                                                                                                                                                         SepaPaymentProviderStateExtractor<HttpResponseBody, PreExecutionResult> providerStateExtractor,
                                                                                                                                                                                         ObjectMapper objectMapper,
                                                                                                                                                                                         Clock clock,
                                                                                                                                                                                         Class<HttpResponseBody> httpResponseBodyClass) {
        return new SepaSubmitPaymentExecutionContextAdapterBuilder<>(
                paymentStatusesExtractor,
                paymentIdExtractor,
                providerStateExtractor,
                objectMapper,
                clock,
                httpResponseBodyClass);
    }

    public SepaSubmitPaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> withHttpRequestBodyProvider(PaymentExecutionHttpRequestBodyProvider<PreExecutionResult, HttpRequestBody> httpRequestBodyProvider) {
        this.httpRequestBodyProvider = httpRequestBodyProvider;
        return this;
    }

    public SepaSubmitPaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> withHttpHeadersProvider(PaymentExecutionHttpHeadersProvider<PreExecutionResult, HttpRequestBody> httpHeadersProvider) {
        this.httpHeadersProvider = httpHeadersProvider;
        return this;
    }

    public SepaSubmitPaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> withHttpRequestInvoker(PaymentHttpRequestInvoker<HttpRequestBody, PreExecutionResult> httpRequestInvoker) {
        this.httpRequestInvoker = httpRequestInvoker;
        return this;
    }

    public SepaSubmitPaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> withResponseBodyValidator(PaymentExecutionResponseBodyValidator<HttpResponseBody> responseBodyValidator) {
        this.responseBodyValidator = responseBodyValidator;
        return this;
    }

    public SepaSubmitPaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> withPreExecutionResultMapper(SepaSubmitPaymentPreExecutionResultMapper<PreExecutionResult> preExecutionResultMapper) {
        this.preExecutionResultMapper = preExecutionResultMapper;
        return this;
    }

    public SepaSubmitPaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> withGetStatusAsSubmitStep() {
        this.getStatusAsSubmitStep = true;
        return this;
    }

    public SepaSubmitPaymentExecutionContextAdapter<HttpRequestBody, HttpResponseBody, PreExecutionResult> build(RawBankPaymentStatusMapper rawBankPaymentStatusMapper) {
        PaymentExecutionContextErrorHandler errorHandler = getStatusAsSubmitStep ? new DefaultGetPaymentStatusExecutionContextErrorHandler(rawBankPaymentStatusMapper, this.objectMapper, this.clock)
                : new DefaultSubmitPaymentExecutionContextErrorHandler(rawBankPaymentStatusMapper, this.objectMapper, this.clock);
        return buildWithCustomErrorHandlers(new DefaultPaymentExecutionContextPreExecutionErrorHandler(PaymentExecutionTechnicalException::paymentSubmissionException),
                errorHandler);
    }

    public SepaSubmitPaymentExecutionContextAdapter<HttpRequestBody, HttpResponseBody, PreExecutionResult> buildWithCustomErrorHandler(PaymentExecutionContextErrorHandler customErrorHandler) {
        return buildWithCustomErrorHandlers(new DefaultPaymentExecutionContextPreExecutionErrorHandler(PaymentExecutionTechnicalException::paymentSubmissionException),
                customErrorHandler);
    }

    public SepaSubmitPaymentExecutionContextAdapter<HttpRequestBody, HttpResponseBody, PreExecutionResult> buildWithCustomPreExecutionErrorHandler(PaymentExecutionContextPreExecutionErrorHandler customPreExecutionErrorHandler,
                                                                                                                                                   RawBankPaymentStatusMapper rawBankPaymentStatusMapper) {
        PaymentExecutionContextErrorHandler errorHandler = getStatusAsSubmitStep ? new DefaultGetPaymentStatusExecutionContextErrorHandler(rawBankPaymentStatusMapper, this.objectMapper, this.clock)
                : new DefaultSubmitPaymentExecutionContextErrorHandler(rawBankPaymentStatusMapper, this.objectMapper, this.clock);
        return buildWithCustomErrorHandlers(customPreExecutionErrorHandler,
                errorHandler);
    }

    public SepaSubmitPaymentExecutionContextAdapter<HttpRequestBody, HttpResponseBody, PreExecutionResult> buildWithCustomErrorHandlers(PaymentExecutionContextPreExecutionErrorHandler customPreExecutionErrorHandler,
                                                                                                                                        PaymentExecutionContextErrorHandler customErrorHandler) {
        return new SepaSubmitPaymentExecutionContextAdapter<>(
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
                this.paymentIdExtractor,
                this.providerStateExtractor
        );
    }
}
