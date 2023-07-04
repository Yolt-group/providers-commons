package com.yolt.providers.common.pis.ukdomestic;

import com.yolt.providers.common.pis.paymentexecutioncontext.model.PaymentExecutionContextMetadata;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class InitiateUkDomesticPaymentResponseDTO {

    @NotNull
    String loginUrl;

    String providerState;

    @Nullable
    PaymentExecutionContextMetadata paymentExecutionContextMetadata;

    public InitiateUkDomesticPaymentResponseDTO(@NotNull String loginUrl, String providerState) {
        this.loginUrl = loginUrl;
        this.providerState = providerState;
    }
}
