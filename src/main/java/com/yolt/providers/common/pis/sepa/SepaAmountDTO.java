package com.yolt.providers.common.pis.sepa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class SepaAmountDTO {

    @NotNull
    private final BigDecimal amount;
}
