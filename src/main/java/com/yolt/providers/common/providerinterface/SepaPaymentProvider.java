package com.yolt.providers.common.providerinterface;

import com.yolt.providers.common.exception.PaymentMethodNotImplementedException;
import com.yolt.providers.common.pis.sepa.*;
import nl.ing.lovebird.providerdomain.ServiceType;

/**
 * SEPA payment flow.
 * Following 2 steps should be supported by all SEPA payment provider implementations:
 * <ul>
 * <li>Initiate a payment</li>
 * <li>Submit (finalize) a payment</li>
 * </ul>
 * <p>
 * According to Berlin standard specification, banks may decide to execute payment on initiation step after user logs
 * in to the bank and confirms payment request. In such cases payment submission is not required as payment is already
 * executed. In order to equally support both, there are 2 flows available:
 *
 * <ul>
 * <li>
 * When submission is required
 * <ul>
 * <li>Yolt initiates payment at provider</li>
 * <li>Provider returns loginUrl only</li>
 * <li>Yolt submits payment after user authenticates and confirms payment</li>
 * </ul>
 * </li>
 * <li>
 * When submission is not required
 * <ul>
 * <li>Yolt initiates payment at provider</li>
 * <li>Provider returns loginUrl + providerState</li>
 * <li>Yolt does not submit payment, but instead parses providerState and returns payment status</li>
 * </ul>
 * </li>
 * </ul>
 */
public interface SepaPaymentProvider extends Provider {

    default ServiceType getServiceType() {
        return ServiceType.PIS;
    }

    /**
     * Initiate payment request
     *
     * @param initiatePaymentRequest - payment initiation info
     * @return - loginUrl and optional providerState (which is used in {@link #submitPayment(SubmitPaymentRequest)}
     * afterwards)
     */
    LoginUrlAndStateDTO initiatePayment(final InitiatePaymentRequest initiatePaymentRequest);

    /**
     * Initiate scheduled payment request
     *
     * @param initiateScheduledPaymentRequest - payment initiation info
     * @return - loginUrl and optional providerState (which is used in {@link #submitPayment(SubmitPaymentRequest)}
     * afterwards)
     */
    default LoginUrlAndStateDTO initiateScheduledPayment(final InitiatePaymentRequest initiateScheduledPaymentRequest) {
        throw new PaymentMethodNotImplementedException();
    }

    /**
     * Initiate periodic payment request
     *
     * @param initiatePeriodicPaymentRequest - payment initiation info
     * @return - loginUrl and optional providerState (which is used in {@link #submitPayment(SubmitPaymentRequest)}
     * afterwards)
     */
    default LoginUrlAndStateDTO initiatePeriodicPayment(final InitiatePaymentRequest initiatePeriodicPaymentRequest) {
        throw new PaymentMethodNotImplementedException();
    }

    /**
     * Submit payment request (when providerState is passed, it is parsed and returned instead of doing actual
     * submission at provider)
     *
     * @param submitPaymentRequest - payment submission info
     * @return - payment ID and payment status
     */
    SepaPaymentStatusResponseDTO submitPayment(final SubmitPaymentRequest submitPaymentRequest);

    /**
     * Gets that status of a payment initiation.
     *
     * @param getStatusRequest - The request object. This is an object because it's easier to add variables in a non-breaking way.
     * @return - payment ID and payment status
     */
    SepaPaymentStatusResponseDTO getStatus(final GetStatusRequest getStatusRequest);
}
