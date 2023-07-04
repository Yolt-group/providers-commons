package com.yolt.providers.common.exception;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import nl.ing.lovebird.providershared.form.Form;

import java.time.Instant;

@Getter
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
public class MfaNeededException extends HandledProviderCheckedException {
    /**
     * Raw JSON response from provider - dataprovider specific format; for submission to the provider.
     */
    private final String providerMfaForm;

    /**
     * Internal MfaForm - generic format; for conversion to DTO objects sent to Yolt app / clients.
     */
    private final transient Form yoltMfaForm;

    private final Instant mfaTimeout;

}
