package com.yolt.providers.common.pis.ukdomestic;

import lombok.Value;
import lombok.experimental.NonFinal;
import org.springframework.lang.Nullable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Map;

@Value
@NonFinal
public class InitiateUkDomesticPaymentRequestDTO {
    @NotNull
    String endToEndIdentification;

    @NotNull
    String currencyCode;

    @NotNull
    BigDecimal amount;

    @NotNull
    UkAccountDTO creditorAccount;

    @Valid
    @Nullable
    UkAccountDTO debtorAccount;

    String remittanceInformationUnstructured;

    @Nullable
    Map<String, String> dynamicFields;
}
