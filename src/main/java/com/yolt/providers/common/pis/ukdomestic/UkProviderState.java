package com.yolt.providers.common.pis.ukdomestic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yolt.providers.common.pis.common.PaymentType;
import com.yolt.providers.common.pis.common.SubmitPaymentRequestDTO;
import lombok.Value;

import javax.validation.constraints.NotNull;

/**
 * Used to store data needed to submit/confirm a payment at an OpenBanking supported bank.
 * This object will be serialized and sent to site-management after initiating a payment, see {@link InitiateUkDomesticPaymentResponseDTO#providerState}.
 * When a user submits a payment, site-management will send the serialized object back to the providers, see {@link SubmitPaymentRequestDTO#providerState}.
 */
@Value
public class UkProviderState {
    /**
     * When submitting/confirming a payment, we need to know the consentId. This consentId is used at the bank to check
     * whether the user has given us permission to submit the payment for them.
     */
    @NotNull
    String consentId;

    /**
     * When deserializing the {@link UkProviderState#openBankingPayment} we need to know which type of payment was serialized in the firs place.
     */
    @NotNull
    PaymentType paymentType;

    /**
     * Uk payments require us to send the initiation object again when we are submitting/confirming a payment.
     * To ensure we can reuse this provider state for all payment types (periodic, scheduled, single domestic)
     * we use an object.
     * <p>
     * Depending on the {@link UkProviderState#paymentType} this object can by any of the following:
     * - OBDomestic1 -- {@link PaymentType#SINGLE}
     * - OBDomesticScheduled1 -- {@link PaymentType#SCHEDULED}
     * - OBDomesticStandingOrder1 -- {@link PaymentType#PERIODIC}
     * Deserializing a raw object using an {@link ObjectMapper} will result in a {@link java.util.Map}.
     * To convert this Map to the correct model you can call the {@link ObjectMapper#convertValue(Object, Class)} method.
     */
    @NotNull
    Object openBankingPayment;
}
