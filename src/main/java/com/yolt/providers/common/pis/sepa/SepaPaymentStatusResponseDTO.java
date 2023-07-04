package com.yolt.providers.common.pis.sepa;

import com.yolt.providers.common.pis.paymentexecutioncontext.model.PaymentExecutionContextMetadata;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.lang.Nullable;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class SepaPaymentStatusResponseDTO {

    @Nullable
    String providerState;

    @NonNull
    private final String paymentId;

    @Nullable
    private PaymentExecutionContextMetadata paymentExecutionContextMetadata;

    public SepaPaymentStatusResponseDTO(@NonNull String paymentId) {
        this.paymentId = paymentId;
    }

    public SepaPaymentStatusResponseDTO(@NonNull String paymentId, PaymentExecutionContextMetadata paymentExecutionContextMetadata) {
        this.paymentId = paymentId;
        this.paymentExecutionContextMetadata = paymentExecutionContextMetadata;
    }
}
