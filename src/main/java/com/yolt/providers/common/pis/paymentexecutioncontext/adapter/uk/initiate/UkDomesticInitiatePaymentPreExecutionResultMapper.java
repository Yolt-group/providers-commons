package com.yolt.providers.common.pis.paymentexecutioncontext.adapter.uk.initiate;

import com.yolt.providers.common.pis.paymentexecutioncontext.model.PaymentPreExecutionResultMapper;
import com.yolt.providers.common.pis.ukdomestic.InitiateUkDomesticPaymentRequest;

/**
 * @deprecated Use {@link UkInitiateSinglePaymentPreExecutionResultMapper} instead
 * @param <PreExecutionResult>
 */
@Deprecated
public interface UkDomesticInitiatePaymentPreExecutionResultMapper<PreExecutionResult> extends PaymentPreExecutionResultMapper<InitiateUkDomesticPaymentRequest, PreExecutionResult> {
}
