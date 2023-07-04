package com.yolt.providers.common.pis.sepa;

import com.yolt.providers.common.pis.paymentexecutioncontext.model.PaymentExecutionContextMetadata;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

/**
 * Would be returned as a response for SEPA initiate payment.
 */
@Getter
@AllArgsConstructor
public class LoginUrlAndStateDTO {

    /**
     * Represents a URL (at a bank) to which user should be redirect to authenticate and authorize payment.
     */
    @NotNull
    String loginUrl;

    /**
     * Optional generic additional data that may be returned in response when initiating a payment (usually in form of JSON).
     * <p>
     * Check {@link com.yolt.providers.common.providerinterface.SepaPaymentProvider} for more details
     */
    @Nullable
    String providerState;

    @Nullable
    PaymentExecutionContextMetadata paymentExecutionContextMetadata;

    public LoginUrlAndStateDTO(@NotNull String loginUrl, @Nullable String providerState) {
        this.loginUrl = loginUrl;
        this.providerState = providerState;
    }
}
