package com.yolt.providers.common.pis.paymentexecutioncontext.adapter.sepa.submit;

import com.yolt.providers.common.pis.paymentexecutioncontext.CommonPaymentExecutionContext;
import com.yolt.providers.common.pis.paymentexecutioncontext.adapter.sepa.initiate.SepaPaymentProviderStateExtractor;
import com.yolt.providers.common.pis.paymentexecutioncontext.exception.PaymentExecutionTechnicalException;
import com.yolt.providers.common.pis.paymentexecutioncontext.model.PaymentExecutionResult;
import com.yolt.providers.common.pis.paymentexecutioncontext.model.PaymentIdExtractor;
import com.yolt.providers.common.pis.sepa.SepaPaymentStatusResponseDTO;
import com.yolt.providers.common.pis.sepa.SubmitPaymentRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SepaSubmitPaymentExecutionContextAdapter<HttpRequestBody, HttpResponseBody, PreExecutionResult> {

    private final CommonPaymentExecutionContext<HttpRequestBody, HttpResponseBody, PreExecutionResult> paymentExecutionContext;
    private final SepaSubmitPaymentPreExecutionResultMapper<PreExecutionResult> preExecutionResultMapper;
    private final PaymentIdExtractor<HttpResponseBody, PreExecutionResult> paymentIdExtractor;
    private final SepaPaymentProviderStateExtractor<HttpResponseBody, PreExecutionResult> providerStateExtractor;

    public SepaPaymentStatusResponseDTO submitPayment(SubmitPaymentRequest submitPaymentRequest) {
        PaymentExecutionResult<HttpResponseBody, PreExecutionResult> result = paymentExecutionContext.execute(
                () -> preExecutionResultMapper.map(submitPaymentRequest)
        );
        return result.getHttpResponseBody()
                .map(response -> {
                    try {
                        return new SepaPaymentStatusResponseDTO(
                                providerStateExtractor.extractProviderState(response, result.getPreExecutionResult()),
                                paymentIdExtractor.extractPaymentId(response, result.getPreExecutionResult()),
                                result.toMetadata()
                        );
                    } catch (Exception ex) {
                        throw PaymentExecutionTechnicalException.paymentSubmissionException(ex);
                    }
                }).orElseGet(() -> new SepaPaymentStatusResponseDTO(submitPaymentRequest.getProviderState(),
                        "",
                        result.toMetadata()));
    }
}
