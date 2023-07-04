package com.yolt.providers.common.pis.common;

import lombok.Value;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Value
public class UkPeriodicPaymentInfo {

    @NotNull
    LocalDate startDate;

    @Nullable
    LocalDate endDate;

    @NotNull
    PeriodicPaymentFrequency frequency;
}
