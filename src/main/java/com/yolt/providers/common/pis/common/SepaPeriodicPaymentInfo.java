package com.yolt.providers.common.pis.common;

import lombok.Builder;
import lombok.Value;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Value
@Builder
public class SepaPeriodicPaymentInfo {

    @NotNull
    LocalDate startDate;

    @Nullable
    LocalDate endDate;

    @NotNull
    PeriodicPaymentFrequency frequency;

    //Defines the behavior when recurring payment dates falls on a weekend or bank holiday.
    @Nullable
    PeriodicPaymentExecutionRule executionRule;
}
