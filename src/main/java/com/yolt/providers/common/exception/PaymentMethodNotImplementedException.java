package com.yolt.providers.common.exception;

import com.yolt.providers.common.providerinterface.PaymentSubmissionProvider;
import com.yolt.providers.common.providerinterface.UkDomesticPaymentProvider;

/**
 * Convenience exception to help migrating providers to a new payment interface.
 * Will be thrown by a provider implementing {@link UkDomesticPaymentProvider} or {@link PaymentSubmissionProvider}.
 */
public class PaymentMethodNotImplementedException extends RuntimeException {
    public PaymentMethodNotImplementedException() {
        super("Method not yet implemented for this provider.");
    }
}
