package com.yolt.providers.common.providerinterface;

import com.yolt.providers.common.domain.ProviderMetaData;
import com.yolt.providers.common.domain.authenticationmeans.TypedAuthenticationMeans;
import com.yolt.providers.common.domain.authenticationmeans.keymaterial.KeyRequirements;
import com.yolt.providers.common.domain.consenttesting.ConsentValidityRules;
import com.yolt.providers.common.versioning.ProviderVersion;
import nl.ing.lovebird.providerdomain.ServiceType;

import java.util.Map;
import java.util.Optional;

public interface Provider {

    /**
     * Return a uniquely identifying strin
     * @return
     */
    String getProviderIdentifier();

    /**
     * Returns a display name that uniquely identifies the provider.
     * @return
     */
    String getProviderIdentifierDisplayName();

    /**
     * Gets the service type which this provider is catering for.
     */
    ServiceType getServiceType();

    /**
     * Gets specific version of provider's implementation
     */
    ProviderVersion getVersion();

    /**
     * This method is called for a list of definition of typed authentication means.
     */
    Map<String, TypedAuthenticationMeans> getTypedAuthenticationMeans();

    /**
     * Returns set of rules for automatic consent validation for given provider.
     */
    ConsentValidityRules getConsentValidityRules();

    /**
     * Returns the additional Metadata for this provider.
     */
    default ProviderMetaData getProviderMetadata() {
        return ProviderMetaData.builder().build();
    }

    /**
     * Returns the definition for creating the signing keypair which the provider might require.
     */
    default Optional<KeyRequirements> getSigningKeyRequirements() {
        return Optional.empty();
    }

    /**
     * Returns the definition for creating the mutual TLS keypair which the provider might require.
     */
    default Optional<KeyRequirements> getTransportKeyRequirements() {
        return Optional.empty();
    }
}
