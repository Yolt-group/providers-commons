package com.yolt.providers.common.providerinterface;

import com.yolt.providers.common.ais.url.UrlAutoOnboardingRequest;
import com.yolt.providers.common.domain.authenticationmeans.BasicAuthenticationMean;
import com.yolt.providers.common.domain.authenticationmeans.TypedAuthenticationMeans;

import java.util.Map;

/**
 * AutoOnboardingProvider marks a provider as being auto-configurable.
 * 
 * The means returned from {@link #getAutoConfiguredMeans} will be configured
 * during the auto-onboarding implementated in
 * {@link #autoConfigureMeans(UrlAutoOnboardingRequest)}.
 */
public interface AutoOnboardingProvider {

    /**
     * These typed authentication means can (and should) be automatically configured.
     * Depending on the implementation, any configured value here may be overwritten.
     *
     * @return the mean(s) that this auto-onboarding will take care of.
     */
    Map<String, TypedAuthenticationMeans> getAutoConfiguredMeans();

    /**
     * Auto configure (part of) the means during onboarding of this provider.
     *
     * @param urlAutoOnboardingRequest the auto-onboarding request.
     * @return the given means complemented with the auto-onboarding configured mean(s).
     */
    Map<String, BasicAuthenticationMean> autoConfigureMeans(final UrlAutoOnboardingRequest urlAutoOnboardingRequest);

    /**
     * Remove the auto configuration at the provider.
     *
     * Depending on the implementation, any existing registration might be:
     * - Deleted completely at the provider;
     * - Fetched, remove one redirectUri and updated at the provider;
     * - Fetched, if last redirectUri, delete completely at the provider.
     *
     * @param urlAutoOnboardingRequest the auto-onboarding request.
     */
    default void removeAutoConfiguration(final UrlAutoOnboardingRequest urlAutoOnboardingRequest) {}
}
