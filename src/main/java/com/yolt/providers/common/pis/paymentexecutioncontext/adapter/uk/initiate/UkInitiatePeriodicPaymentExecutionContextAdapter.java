package com.yolt.providers.common.pis.paymentexecutioncontext.adapter.uk.initiate;

import com.yolt.providers.common.pis.ukdomestic.InitiateUkDomesticPaymentResponseDTO;
import com.yolt.providers.common.pis.ukdomestic.InitiateUkDomesticPeriodicPaymentRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UkInitiatePeriodicPaymentExecutionContextAdapter<HttpRequestBody, HttpResponseBody, PreExecutionResult> {

    private final CommonUkInitiatePaymentExecutionContextAdapter<InitiateUkDomesticPeriodicPaymentRequest, HttpRequestBody, HttpResponseBody, PreExecutionResult> commonUkInitiatePaymentExecutionContextAdapter;

    public InitiateUkDomesticPaymentResponseDTO initiatePeriodicPayment(InitiateUkDomesticPeriodicPaymentRequest initiateRequest) {
        return commonUkInitiatePaymentExecutionContextAdapter.initiatePayment(initiateRequest);
    }
}
