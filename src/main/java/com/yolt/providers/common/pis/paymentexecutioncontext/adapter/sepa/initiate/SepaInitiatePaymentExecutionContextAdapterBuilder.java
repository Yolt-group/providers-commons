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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.time.Clock;

/**
 * @deprecated Use {@link SepaInitiateSinglePaymentExecutionContextAdapterBuilder} instead
 * @param <HttpRequestBody>
 * @param <HttpResponseBody>
 * @param <PreExecutionResult>
 */
@Deprecated
@RequiredArgsConstructor
public class SepaInitiatePaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> {

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
    private SepaInitiatePaymentPreExecutionResultMapper<PreExecutionResult> preExecutionResultMapper = initiatePaymentRequest -> null;

    public static <HttpRequestBody, HttpResponseBody, PreExecutionResult> SepaInitiatePaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> builder(PaymentStatusesExtractor<HttpResponseBody, PreExecutionResult> paymentStatusesExtractor,
                                                                                                                                                                                           PaymentAuthorizationUrlExtractor<HttpResponseBody, PreExecutionResult> authorizationUrlExtractor,
                                                                                                                                                                                           SepaPaymentProviderStateExtractor<HttpResponseBody, PreExecutionResult> providerStateExtractor,
                                                                                                                                                                                           ObjectMapper objectMapper,
                                                                                                                                                                                           Clock clock,
                                                                                                                                                                                           Class<HttpResponseBody> httpResponseBodyClass) {
        return new SepaInitiatePaymentExecutionContextAdapterBuilder<>(
                paymentStatusesExtractor,
                authorizationUrlExtractor,
                providerStateExtractor,
                objectMapper,
                clock,
                httpResponseBodyClass);
    }

    public SepaInitiatePaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> withHttpRequestBodyProvider(PaymentExecutionHttpRequestBodyProvider<PreExecutionResult, HttpRequestBody> httpRequestBodyProvider) {
        this.httpRequestBodyProvider = httpRequestBodyProvider;
        return this;
    }

    public SepaInitiatePaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> withHttpHeadersProvider(PaymentExecutionHttpHeadersProvider<PreExecutionResult, HttpRequestBody> httpHeadersProvider) {
        this.httpHeadersProvider = httpHeadersProvider;
        return this;
    }

    public SepaInitiatePaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> withHttpRequestInvoker(PaymentHttpRequestInvoker<HttpRequestBody, PreExecutionResult> httpRequestInvoker) {
        this.httpRequestInvoker = httpRequestInvoker;
        return this;
    }

    public SepaInitiatePaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> withResponseBodyValidator(PaymentExecutionResponseBodyValidator<HttpResponseBody> responseBodyValidator) {
        this.responseBodyValidator = responseBodyValidator;
        return this;
    }

    public SepaInitiatePaymentExecutionContextAdapterBuilder<HttpRequestBody, HttpResponseBody, PreExecutionResult> withPreExecutionResultMapper(SepaInitiatePaymentPreExecutionResultMapper<PreExecutionResult> preExecutionResultMapper) {
        this.preExecutionResultMapper = preExecutionResultMapper;
        return this;
    }

    public SepaInitiatePaymentExecutionContextAdapter<HttpRequestBody, HttpResponseBody, PreExecutionResult> build(RawBankPaymentStatusMapper rawBankPaymentStatusMapper) {
        return buildWithCustomErrorHandlers(new DefaultPaymentExecutionContextPreExecutionErrorHandler(PaymentExecutionTechnicalException::paymentInitiationException),
                new DefaultInitiatePaymentExecutionContextErrorHandler(
                        rawBankPaymentStatusMapper,
                        this.objectMapper,
                        this.clock
                ));
    }

    public SepaInitiatePaymentExecutionContextAdapter<HttpRequestBody, HttpResponseBody, PreExecutionResult> buildWithCustomErrorHandler(PaymentExecutionContextErrorHandler customErrorHandler) {
        return buildWithCustomErrorHandlers(new DefaultPaymentExecutionContextPreExecutionErrorHandler(PaymentExecutionTechnicalException::paymentInitiationException),
                customErrorHandler);
    }

    public SepaInitiatePaymentExecutionContextAdapter<HttpRequestBody, HttpResponseBody, PreExecutionResult> buildWithCustomPreExecutionErrorHandler(PaymentExecutionContextPreExecutionErrorHandler customPreExecutionErrorHandler,
                                                                                                                                                     RawBankPaymentStatusMapper rawBankPaymentStatusMapper) {
        return buildWithCustomErrorHandlers(customPreExecutionErrorHandler,
                new DefaultInitiatePaymentExecutionContextErrorHandler(
                        rawBankPaymentStatusMapper,
                        this.objectMapper,
                        this.clock
                ));
    }

    public SepaInitiatePaymentExecutionContextAdapter<HttpRequestBody, HttpResponseBody, PreExecutionResult> buildWithCustomErrorHandlers(PaymentExecutionContextPreExecutionErrorHandler customPreExecutionErrorHandler,
                                                                                                                                          PaymentExecutionContextErrorHandler customErrorHandler) {
        return new SepaInitiatePaymentExecutionContextAdapter<>(
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
