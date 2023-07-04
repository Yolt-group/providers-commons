package com.yolt.providers.common.pis.paymentexecutioncontext.adapter.uk.initiate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yolt.providers.common.pis.paymentexecutioncontext.CommonPaymentExecutionContext;
import com.yolt.providers.common.pis.paymentexecutioncontext.exception.PaymentExecutionTechnicalException;
import com.yolt.providers.common.pis.paymentexecutioncontext.model.PaymentAuthorizationUrlExtractor;
import com.yolt.providers.common.pis.paymentexecutioncontext.model.PaymentExecutionResult;
import com.yolt.providers.common.pis.ukdomestic.InitiateUkDomesticPaymentRequest;
import com.yolt.providers.common.pis.ukdomestic.InitiateUkDomesticPaymentResponseDTO;
import com.yolt.providers.common.pis.ukdomestic.UkProviderState;
import lombok.RequiredArgsConstructor;

/**
 * @param <HttpRequestBody>
 * @param <HttpResponseBody>
 * @param <PreExecutionResult>
 * @deprecated Use {@link UkInitiateSinglePaymentExecutionContextAdapter} instead
 */
@Deprecated
@RequiredArgsConstructor
public class UkInitiatePaymentExecutionContextAdapter<HttpRequestBody, HttpResponseBody, PreExecutionResult> {

    private final CommonPaymentExecutionContext<HttpRequestBody, HttpResponseBody, PreExecutionResult> paymentExecutionContext;
    private final UkDomesticInitiatePaymentPreExecutionResultMapper<PreExecutionResult> ukDomesticInitiatePaymentPreExecutionResultMapper;
    private final PaymentAuthorizationUrlExtractor<HttpResponseBody, PreExecutionResult> authorizationUrlExtractor;
    private final UkPaymentProviderStateExtractor<HttpResponseBody, PreExecutionResult> ukPaymentProviderStateExtractor;
    private final ObjectMapper objectMapper;

    public InitiateUkDomesticPaymentResponseDTO initiateSinglePayment(InitiateUkDomesticPaymentRequest initiateRequest) {
        PaymentExecutionResult<HttpResponseBody, PreExecutionResult> result = paymentExecutionContext.execute(
                () -> ukDomesticInitiatePaymentPreExecutionResultMapper.map(initiateRequest));
        return result.getHttpResponseBody()
                .map(response -> {
                    try {
                        String authorizationUrl = authorizationUrlExtractor.extractAuthorizationUrl(response, result.getPreExecutionResult());
                        UkProviderState ukProviderState = ukPaymentProviderStateExtractor.extractUkProviderState(response, result.getPreExecutionResult());
                        String providerState = objectMapper.writeValueAsString(ukProviderState);
                        return new InitiateUkDomesticPaymentResponseDTO(
                                authorizationUrl,
                                providerState,
                                result.toMetadata()
                        );
                    } catch (Exception ex) {
                        throw PaymentExecutionTechnicalException.paymentInitiationException(ex);
                    }
                }).orElseGet(() -> new InitiateUkDomesticPaymentResponseDTO("", "", result.toMetadata()));
    }
}
