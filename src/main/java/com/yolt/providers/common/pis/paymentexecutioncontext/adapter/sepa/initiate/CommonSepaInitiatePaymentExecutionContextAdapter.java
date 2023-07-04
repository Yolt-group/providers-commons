package com.yolt.providers.common.pis.paymentexecutioncontext.adapter.sepa.initiate;

import com.yolt.providers.common.pis.paymentexecutioncontext.CommonPaymentExecutionContext;
import com.yolt.providers.common.pis.paymentexecutioncontext.exception.PaymentExecutionTechnicalException;
import com.yolt.providers.common.pis.paymentexecutioncontext.model.PaymentAuthorizationUrlExtractor;
import com.yolt.providers.common.pis.paymentexecutioncontext.model.PaymentExecutionResult;
import com.yolt.providers.common.pis.paymentexecutioncontext.model.PaymentPreExecutionResultMapper;
import com.yolt.providers.common.pis.sepa.LoginUrlAndStateDTO;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommonSepaInitiatePaymentExecutionContextAdapter<PaymentRequest, HttpRequestBody, HttpResponseBody, PreExecutionResult> {

    private final CommonPaymentExecutionContext<HttpRequestBody, HttpResponseBody, PreExecutionResult> paymentExecutionContext;
    private final PaymentPreExecutionResultMapper<PaymentRequest, PreExecutionResult> preExecutionResultMapper;
    private final PaymentAuthorizationUrlExtractor<HttpResponseBody, PreExecutionResult> authorizationUrlExtractor;
    private final SepaPaymentProviderStateExtractor<HttpResponseBody, PreExecutionResult> providerStateExtractor;

    public LoginUrlAndStateDTO initiatePayment(final PaymentRequest paymentRequest) {
        PaymentExecutionResult<HttpResponseBody, PreExecutionResult> result = paymentExecutionContext.execute(
                () -> preExecutionResultMapper.map(paymentRequest)
        );
        return result.getHttpResponseBody()
                .map(response -> {
                    try {
                        return new LoginUrlAndStateDTO(
                                authorizationUrlExtractor.extractAuthorizationUrl(response, result.getPreExecutionResult()),
                                providerStateExtractor.extractProviderState(response, result.getPreExecutionResult()),
                                result.toMetadata()
                        );
                    } catch (Exception ex) {
                        throw PaymentExecutionTechnicalException.paymentInitiationException(ex);
                    }
                }).orElseGet(() -> new LoginUrlAndStateDTO("", "", result.toMetadata()));
    }
}
