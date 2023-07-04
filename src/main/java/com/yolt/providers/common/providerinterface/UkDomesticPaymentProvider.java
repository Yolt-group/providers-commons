package com.yolt.providers.common.providerinterface;

import com.yolt.providers.common.exception.CreationFailedException;
import com.yolt.providers.common.exception.PaymentMethodNotImplementedException;
import com.yolt.providers.common.pis.common.SubmitPaymentRequest;
import com.yolt.providers.common.pis.ukdomestic.InitiateUkDomesticPaymentRequest;
import com.yolt.providers.common.pis.ukdomestic.InitiateUkDomesticPaymentResponseDTO;
import com.yolt.providers.common.pis.ukdomestic.InitiateUkDomesticPeriodicPaymentRequest;
import com.yolt.providers.common.pis.ukdomestic.InitiateUkDomesticScheduledPaymentRequest;
import nl.ing.lovebird.providerdomain.ServiceType;

/**
 * The UkDomesticPaymentProvider provides all methods to initiate Uk Domestic payments.
 * Supported Uk Domestic payment types:
 * - Single Payment
 * - Scheduled Payment
 * - Periodic Payment
 * <p>
 * Because we can use the provider state stored in site-management to keep track of the data that needs to be sent when
 * approving/submitting the actual payment we can use a generic return type for initiating payments.
 * It also allows us to use a generic payment submission method because all required data for the specific payment can
 * be stored in the provider state after the payment is initiated.
 */
public interface UkDomesticPaymentProvider extends Provider {

    default ServiceType getServiceType() {
        return ServiceType.PIS;
    }

    /**
     * Initiate single domestic UK payment request
     *
     * @param initiatePaymentRequest - payment initiation info for a single payment
     * @return - loginUrl and optional providerState (which is used in {@link PaymentSubmissionProvider#submitPayment(SubmitPaymentRequest)}
     * afterwards)
     */
    default InitiateUkDomesticPaymentResponseDTO initiateSinglePayment(final InitiateUkDomesticPaymentRequest initiatePaymentRequest) throws CreationFailedException {
        throw new PaymentMethodNotImplementedException();
    }

    /**
     * Initiate scheduled domestic UK payment request
     *
     * @param initiatePaymentRequest - payment initiation info required for a scheduled payment
     * @return - loginUrl and optional providerState (which is used in {@link PaymentSubmissionProvider#submitPayment(SubmitPaymentRequest)}
     * afterwards)
     */
    default InitiateUkDomesticPaymentResponseDTO initiateScheduledPayment(final InitiateUkDomesticScheduledPaymentRequest initiatePaymentRequest) throws CreationFailedException {
        throw new PaymentMethodNotImplementedException();
    }

    /**
     * Initiate scheduled domestic UK payment request
     *
     * @param initiatePaymentRequest - payment initiation info required for a periodic payment
     * @return - loginUrl and optional providerState (which is used in {@link PaymentSubmissionProvider#submitPayment(SubmitPaymentRequest)}
     * afterwards)
     */
    default InitiateUkDomesticPaymentResponseDTO initiatePeriodicPayment(final InitiateUkDomesticPeriodicPaymentRequest initiatePaymentRequest) throws CreationFailedException {
        throw new PaymentMethodNotImplementedException();
    }
}
