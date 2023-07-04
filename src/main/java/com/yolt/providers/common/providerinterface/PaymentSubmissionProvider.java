package com.yolt.providers.common.providerinterface;

import com.yolt.providers.common.exception.ConfirmationFailedException;
import com.yolt.providers.common.exception.PaymentMethodNotImplementedException;
import com.yolt.providers.common.pis.common.GetStatusRequest;
import com.yolt.providers.common.pis.common.PaymentStatusResponseDTO;
import com.yolt.providers.common.pis.common.SubmitPaymentRequest;
import nl.ing.lovebird.providerdomain.ServiceType;

/**
 * A generic interface for submitting payments.
 * Most SEPA and OpenBanking providers require the same fields for submitting a payment.
 * By using this interface no distinction has to be made between both.
 */
public interface PaymentSubmissionProvider extends Provider {
    default ServiceType getServiceType() {
        return ServiceType.PIS;
    }

    /**
     * Submit payment request (when providerState is passed, it is parsed and returned instead of doing actual
     * submission at provider)
     *
     * @param submitPaymentRequest - payment submission info
     * @return - payment ID and payment status
     */
    default PaymentStatusResponseDTO submitPayment(final SubmitPaymentRequest submitPaymentRequest) throws ConfirmationFailedException {
        throw new PaymentMethodNotImplementedException();
    }

    /**
     * Gets the status of a payment.
     *
     * @param getStatusRequest - The request object. This is an object because it's easier to add variables in a non-breaking way.
     * @return - payment ID and payment status
     */
    default PaymentStatusResponseDTO getStatus(final GetStatusRequest getStatusRequest) {
        return new PaymentStatusResponseDTO(getStatusRequest.getProviderState(), getStatusRequest.getPaymentId());
    }
}
