package com.yolt.providers.common.pis.paymentexecutioncontext.adapter.sepa.status;

import com.yolt.providers.common.pis.paymentexecutioncontext.CommonPaymentExecutionContext;
import com.yolt.providers.common.pis.paymentexecutioncontext.adapter.sepa.initiate.SepaPaymentProviderStateExtractor;
import com.yolt.providers.common.pis.paymentexecutioncontext.exception.PaymentExecutionTechnicalException;
import com.yolt.providers.common.pis.paymentexecutioncontext.model.PaymentExecutionResult;
import com.yolt.providers.common.pis.paymentexecutioncontext.model.PaymentIdExtractor;
import com.yolt.providers.common.pis.sepa.GetStatusRequest;
import com.yolt.providers.common.pis.sepa.SepaPaymentStatusResponseDTO;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SepaStatusPaymentExecutionContextAdapter<HttpRequestBody, HttpResponseBody, PreExecutionResult> {

    private final CommonPaymentExecutionContext<HttpRequestBody, HttpResponseBody, PreExecutionResult> paymentExecutionContext;
    private final SepaStatusPaymentPreExecutionResultMapper<PreExecutionResult> preExecutionResultMapper;
    private final PaymentIdExtractor<HttpResponseBody, PreExecutionResult> paymentIdExtractor;
    private final SepaPaymentProviderStateExtractor<HttpResponseBody, PreExecutionResult> providerStateExtractor;

    public SepaPaymentStatusResponseDTO getPaymentStatus(GetStatusRequest getStatusRequest) {
        PaymentExecutionResult<HttpResponseBody, PreExecutionResult> result = paymentExecutionContext.execute(
                () -> preExecutionResultMapper.map(getStatusRequest)
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
                        throw PaymentExecutionTechnicalException.statusFailed(ex);
                    }
                }).orElseGet(() -> new SepaPaymentStatusResponseDTO(getStatusRequest.getProviderState(),
                        "",
                        result.toMetadata()));
    }
}
