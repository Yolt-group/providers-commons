package com.yolt.providers.common.pis.ukdomestic;

import lombok.Value;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

@Value
public class UkAccountDTO {
    @NotNull
    String accountIdentifier;

    @NotNull
    AccountIdentifierScheme accountIdentifierScheme;

    @NotNull
    String accountHolderName;

    @Nullable
    String secondaryIdentification;
}
