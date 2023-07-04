package com.yolt.providers.common.pis.sepa;

import com.yolt.providers.common.pis.common.SepaPeriodicPaymentInfo;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class SepaInitiatePaymentRequestDTO {

    @NotNull
    private final SepaAccountDTO creditorAccount;

    @NotNull
    @Size(min = 2, max = 70)
    private final String creditorName;

    @NotNull
    @Pattern(regexp = "[A-Za-z0-9-]{1,35}")
    private final String endToEndIdentification;

    @NotNull
    private final SepaAmountDTO instructedAmount;

    @NotNull
    @Size(max = 140)
    private final String remittanceInformationUnstructured;

    /**
     * Optional fields below.
     * The siteslist should indicate whether these properties are supported and/or required.
     */

    @Nullable
    private final SepaAccountDTO debtorAccount;

    /** Date at which the payment should be executed. Scheduled payments only. */
    @Nullable
    private LocalDate executionDate;

    /** Recurring schedule for executing the payment. Periodic payments only. */
    @Nullable
    private SepaPeriodicPaymentInfo periodicPaymentInfo;

    @Nullable
    private InstructionPriority instructionPriority;

    @Nullable
    private DynamicFields dynamicFields;
}
