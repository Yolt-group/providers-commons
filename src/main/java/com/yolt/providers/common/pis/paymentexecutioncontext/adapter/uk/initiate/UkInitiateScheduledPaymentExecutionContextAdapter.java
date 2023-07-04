package com.yolt.providers.common.pis.paymentexecutioncontext.adapter.uk.initiate;

import com.yolt.providers.common.pis.ukdomestic.InitiateUkDomesticPaymentResponseDTO;
import com.yolt.providers.common.pis.ukdomestic.InitiateUkDomesticScheduledPaymentRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UkInitiateScheduledPaymentExecutionContextAdapter<HttpRequestBody, HttpResponseBody, PreExecutionResult> {

    private final CommonUkInitiatePaymentExecutionContextAdapter<InitiateUkDomesticScheduledPaymentRequest, HttpRequestBody, HttpResponseBody, PreExecutionResult> commonUkInitiatePaymentExecutionContextAdapter;

    public InitiateUkDomesticPaymentResponseDTO initiateScheduledPayment(InitiateUkDomesticScheduledPaymentRequest initiateRequest) {
        return commonUkInitiatePaymentExecutionContextAdapter.initiatePayment(initiateRequest);
    }
}
