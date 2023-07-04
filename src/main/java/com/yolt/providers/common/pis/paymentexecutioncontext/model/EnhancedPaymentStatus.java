package com.yolt.providers.common.pis.paymentexecutioncontext.model;

import lombok.Getter;

@Getter
public enum EnhancedPaymentStatus {
    /**
     * Technical creation in YTS database
     **/
    CREATED(false),

    /**
     * Initial status. Set when payment successfully initiated at bank (except for Starling), but no consent given yet
     **/
    INITIATION_SUCCESS(false),

    /**
     * Bank returned a 404, 405, 5xx or timeout error during initiation. Or we had a power outage right after CREATED.
     * Payment can't have gone through, because no consent yet. In other words: technical errors at the network or ASPSP.
     **/
    INITIATION_ERROR(true),

    /**
     * RJCT (Rejected) - bank understood but rejected the payment initiation request for any reason. Rejection can happen
     * both during initiation or (in Open Banking) during submit. Possible reasons (non-exhaustive): frequency not
     * supported on a periodic payment, currency not supported, payment sent after the "CutOffDateTime" . We must
     * always include the raw reason why the bank rejected the payment. We need a specific field in the model called
     * 'bankRejectionReason' for this. Note that this status will often be set in response to a 400 error.
     */
    REJECTED(true),

    /**
     * Payment initiated with bank ('INITIATION_SUCCESS'), but after 120 mins no progress. OR, the user returned
     * and in the status call we see that the user declined the payment. The reason for the large, 120 minute, window is
     * here, because some banks may have a session this long. For a bank that doesn't have a submit step, the user might
     * approve the payment after 110 minutes, the payment would get executed by the bank at that point. So at that point,
     * when the user returns to Yolt, we MUST still have an "open session". That is why we set the payment to
     * NO_CONSENT_FROM_USER only after 120 minutes.
     **/
    NO_CONSENT_FROM_USER(true),

    /**
     * Intermediate status. Payment accepted by bank. In progress. Set in the submit stage in Open Banking. But we may
     * have banks where execution starts on the consent page and we discover the ACCEPTED status when we call the status
     * endpoint in the (fake) submit step. We force clients to always call submit, but for banks that don't have a
     * submit step we just get status.
     **/
    ACCEPTED(false),

    /**
     * Timeout during a real submit step. In this case we really don't know what happened to the payment.
     **/
    UNKNOWN(false),

    /**
     * Execution failed: 4xx, 5xx or failure to obtain token
     **/
    EXECUTION_FAILED(true),

    /**
     * payment completed
     **/
    COMPLETED(true);

    private final boolean isTerminalState;

    EnhancedPaymentStatus(boolean terminalState) {
        this.isTerminalState = terminalState;
    }
}
