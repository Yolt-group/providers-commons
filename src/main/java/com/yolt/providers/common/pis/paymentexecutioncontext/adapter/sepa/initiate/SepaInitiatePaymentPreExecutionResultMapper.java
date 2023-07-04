package com.yolt.providers.common.pis.paymentexecutioncontext.adapter.sepa.initiate;

import com.yolt.providers.common.pis.paymentexecutioncontext.model.PaymentPreExecutionResultMapper;
import com.yolt.providers.common.pis.sepa.InitiatePaymentRequest;

/**
 * @deprecated Use {@link SepaInitiateSinglePaymentPreExecutionResultMapper} instead
 * @param <PreExecutionResult>
 */
@Deprecated
public interface SepaInitiatePaymentPreExecutionResultMapper<PreExecutionResult> extends PaymentPreExecutionResultMapper<InitiatePaymentRequest, PreExecutionResult> {

}
