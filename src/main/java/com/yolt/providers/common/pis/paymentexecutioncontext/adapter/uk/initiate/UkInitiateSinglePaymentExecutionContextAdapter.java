package com.yolt.providers.common.pis.paymentexecutioncontext.adapter.uk.initiate;

import com.yolt.providers.common.pis.paymentexecutioncontext.CommonPaymentExecutionContext;
import com.yolt.providers.common.pis.paymentexecutioncontext.model.PaymentAuthorizationUrlExtractor;
import com.yolt.providers.common.pis.ukdomestic.InitiateUkDomesticPaymentRequest;
import com.yolt.providers.common.pis.ukdomestic.InitiateUkDomesticPaymentResponseDTO;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UkInitiateSinglePaymentExecutionContextAdapter<HttpRequestBody, HttpResponseBody, PreExecutionResult> {

    private final CommonPaymentExecutionContext<HttpRequestBody, HttpResponseBody, PreExecutionResult> paymentExecutionContext;
    private final PaymentAuthorizationUrlExtractor<HttpResponseBody, PreExecutionResult> authorizationUrlExtractor;
    private final UkPaymentProviderStateExtractor<HttpResponseBody, PreExecutionResult> ukPaymentProviderStateExtractor;
    private final CommonUkInitiatePaymentExecutionContextAdapter<InitiateUkDomesticPaymentRequest, HttpRequestBody, HttpResponseBody, PreExecutionResult> commonUkInitiatePaymentExecutionContextAdapter;

    public InitiateUkDomesticPaymentResponseDTO initiateSinglePayment(InitiateUkDomesticPaymentRequest initiateRequest) {
        return commonUkInitiatePaymentExecutionContextAdapter.initiatePayment(initiateRequest);
    }
}
