package com.yolt.providers.common.pis.paymentexecutioncontext.adapter.sepa.status;

import com.yolt.providers.common.pis.paymentexecutioncontext.model.PaymentPreExecutionResultMapper;
import com.yolt.providers.common.pis.sepa.GetStatusRequest;

public interface SepaStatusPaymentPreExecutionResultMapper<PreExecutionResult> extends PaymentPreExecutionResultMapper<GetStatusRequest, PreExecutionResult> {
}
