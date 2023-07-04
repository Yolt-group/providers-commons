package com.yolt.providers.common.domain.authenticationmeans;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class BasicAuthenticationMean {

    @NotNull
    protected final AuthenticationMeanType type;

    @NotNull
    protected final String value;
}