package com.yolt.providers.common.domain.authenticationmeans.keymaterial;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class KeyRequirements {
    private final KeyMaterialRequirements keyRequirements;
    private final String privateKidAuthenticationMeanReference;
    private final String publicKeyAuthenticationMeanReference;

    public KeyRequirements(KeyMaterialRequirements keyRequirements, String privateKidAuthenticationMeanReference) {
        this(keyRequirements, privateKidAuthenticationMeanReference, null);
    }
}
