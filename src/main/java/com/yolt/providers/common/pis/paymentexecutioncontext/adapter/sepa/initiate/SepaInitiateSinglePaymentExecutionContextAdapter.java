package com.yolt.providers.common.pis.paymentexecutioncontext.adapter.sepa.initiate;

import com.yolt.providers.common.pis.sepa.InitiatePaymentRequest;
import com.yolt.providers.common.pis.sepa.LoginUrlAndStateDTO;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SepaInitiateSinglePaymentExecutionContextAdapter<HttpRequestBody, HttpResponseBody, PreExecutionResult> {

    private final CommonSepaInitiatePaymentExecutionContextAdapter<InitiatePaymentRequest, HttpRequestBody, HttpResponseBody, PreExecutionResult> commonSepaInitiatePaymentExecutionContextAdapter;

    public LoginUrlAndStateDTO initiateSinglePayment(InitiatePaymentRequest paymentRequest) {
        return commonSepaInitiatePaymentExecutionContextAdapter.initiatePayment(paymentRequest);
    }
}
