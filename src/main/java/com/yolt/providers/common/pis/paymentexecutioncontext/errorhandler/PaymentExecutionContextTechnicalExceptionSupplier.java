package com.yolt.providers.common.pis.paymentexecutioncontext.errorhandler;

import com.yolt.providers.common.pis.paymentexecutioncontext.exception.PaymentExecutionTechnicalException;

public interface PaymentExecutionContextTechnicalExceptionSupplier {

    PaymentExecutionTechnicalException createPaymentExecutionTechnicalException(Throwable ex);
}
