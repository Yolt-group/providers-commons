package com.yolt.providers.common.pis.common;

import com.yolt.providers.common.pis.paymentexecutioncontext.model.PaymentExecutionContextMetadata;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class PaymentStatusResponseDTO {

    @NotNull
    String providerState;
    @NotNull
    String paymentId;

    @Nullable
    PaymentExecutionContextMetadata paymentExecutionContextMetadata;

    public PaymentStatusResponseDTO(@NotNull String providerState, @NotNull String paymentId) {
        this.providerState = providerState;
        this.paymentId = paymentId;
    }
}
