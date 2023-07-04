package com.yolt.providers.common.pis.paymentexecutioncontext.model;

import lombok.NonNull;
import lombok.Value;

/**
 * Captures payment statuses of the bank
 * We need the bank payment status for analysis and support in Site Management
 */
@Value
public class PaymentStatuses {

    @NonNull
    private RawBankPaymentStatus rawBankPaymentStatus;

    @NonNull
    private EnhancedPaymentStatus paymentStatus;

}