package com.yolt.providers.common.pis.sepa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import nl.ing.lovebird.extendeddata.common.CurrencyCode;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class SepaAccountDTO {

    private final CurrencyCode currency;

    @NotNull
    private final String iban;
}
