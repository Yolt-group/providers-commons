package com.yolt.providers.common.pis.paymentexecutioncontext.adapter.uk.submit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yolt.providers.common.pis.common.PaymentStatusResponseDTO;
import com.yolt.providers.common.pis.common.SubmitPaymentRequest;
import com.yolt.providers.common.pis.paymentexecutioncontext.CommonPaymentExecutionContext;
import com.yolt.providers.common.pis.paymentexecutioncontext.adapter.uk.initiate.UkPaymentProviderStateExtractor;
import com.yolt.providers.common.pis.paymentexecutioncontext.exception.PaymentExecutionTechnicalException;
import com.yolt.providers.common.pis.paymentexecutioncontext.model.PaymentExecutionResult;
import com.yolt.providers.common.pis.paymentexecutioncontext.model.PaymentIdExtractor;
import com.yolt.providers.common.pis.ukdomestic.UkProviderState;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UkSubmitPaymentExecutionContextAdapter<HttpRequestBody, HttpResponseBody, PreExecutionResult> {

    private final CommonPaymentExecutionContext<HttpRequestBody, HttpResponseBody, PreExecutionResult> paymentExecutionContext;
    private final UkDomesticSubmitPreExecutionResultMapper<PreExecutionResult> ukDomesticSubmitPreExecutionResultMapper;
    private final PaymentIdExtractor<HttpResponseBody, PreExecutionResult> paymentIdExtractor;
    private final UkPaymentProviderStateExtractor<HttpResponseBody, PreExecutionResult> ukPaymentProviderStateExtractor;
    private final ObjectMapper objectMapper;

    public PaymentStatusResponseDTO submitPayment(SubmitPaymentRequest request) {
        PaymentExecutionResult<HttpResponseBody, PreExecutionResult> result = paymentExecutionContext.execute(
                () -> ukDomesticSubmitPreExecutionResultMapper.map(request));
        return result.getHttpResponseBody()
                .map(response -> {
                    try {
                        UkProviderState ukProviderState = ukPaymentProviderStateExtractor.extractUkProviderState(response, result.getPreExecutionResult());
                        String providerState = objectMapper.writeValueAsString(ukProviderState);
                        return new PaymentStatusResponseDTO(
                                providerState,
                                paymentIdExtractor.extractPaymentId(response, result.getPreExecutionResult()),
                                result.toMetadata());
                    } catch (Exception ex) {
                        throw PaymentExecutionTechnicalException.paymentSubmissionException(ex);
                    }
                })
                .orElseGet(() -> new PaymentStatusResponseDTO(request.getProviderState(),
                        "",
                        result.toMetadata()));
    }
}
