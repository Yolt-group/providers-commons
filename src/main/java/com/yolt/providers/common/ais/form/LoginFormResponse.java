package com.yolt.providers.common.ais.form;

import lombok.Value;
import nl.ing.lovebird.providershared.form.Form;

@Value
public class LoginFormResponse {
    /**
     * Raw JSON response from provider - dataprovider specific format; for submission to the provider.
     */
    private final String providerForm;

    /**
     * Internal Form - generic format; for conversion to DTO objects sent to Yolt app / clients.
     */
    private final Form yoltForm;
}
