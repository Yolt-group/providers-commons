package com.yolt.providers.common.ais.url;

import com.yolt.providers.common.cryptography.RestTemplateManager;
import com.yolt.providers.common.cryptography.Signer;
import com.yolt.providers.common.domain.authenticationmeans.BasicAuthenticationMean;
import lombok.NoArgsConstructor;
import nl.ing.lovebird.providershared.form.FilledInUserSiteFormValues;

import java.util.Map;
import java.util.UUID;

@NoArgsConstructor
public class UrlCreateAccessMeansRequestBuilder {

    private UUID userId;
    private String redirectUrlPostedBackFromSite;
    private String baseClientRedirectUrl;
    private Map<String, BasicAuthenticationMean> authenticationMeans;
    private String providerState;
    private Signer signer;
    private RestTemplateManager restTemplateManager;
    private FilledInUserSiteFormValues filledInUserSiteFormValues;
    private String state;
    private String psuIpAddress;

    public UrlCreateAccessMeansRequest build() {
        return new UrlCreateAccessMeansRequest(userId, redirectUrlPostedBackFromSite, baseClientRedirectUrl, authenticationMeans, providerState,
                signer, restTemplateManager, filledInUserSiteFormValues, state, psuIpAddress);
    }

    public UrlCreateAccessMeansRequestBuilder setUserId(final UUID userId) {
        this.userId = userId;
        return this;
    }

    public UrlCreateAccessMeansRequestBuilder setRedirectUrlPostedBackFromSite(final String redirectUrlPostedBackFromSite) {
        this.redirectUrlPostedBackFromSite = redirectUrlPostedBackFromSite;
        return this;
    }

    public UrlCreateAccessMeansRequestBuilder setAuthenticationMeans(final Map<String, BasicAuthenticationMean> authenticationMeans) {
        this.authenticationMeans = authenticationMeans;
        return this;
    }

    public UrlCreateAccessMeansRequestBuilder setProviderState(final String providerState) {
        this.providerState = providerState;
        return this;
    }

    public UrlCreateAccessMeansRequestBuilder setSigner(final Signer signer) {
        this.signer = signer;
        return this;
    }

    public UrlCreateAccessMeansRequestBuilder setRestTemplateManager(final RestTemplateManager restTemplateManager) {
        this.restTemplateManager = restTemplateManager;
        return this;
    }

    public UrlCreateAccessMeansRequestBuilder setFilledInUserSiteFormValues(final FilledInUserSiteFormValues filledInUserSiteFormValues) {
        this.filledInUserSiteFormValues = filledInUserSiteFormValues;
        return this;
    }

    public UrlCreateAccessMeansRequestBuilder setState(final String state) {
        this.state = state;
        return this;
    }

    public UrlCreateAccessMeansRequestBuilder setPsuIpAddress(final String psuIpAddress) {
        this.psuIpAddress = psuIpAddress;
        return this;
    }

    public UrlCreateAccessMeansRequestBuilder setBaseClientRedirectUrl(final String baseClientRedirectUrl) {
        this.baseClientRedirectUrl = baseClientRedirectUrl;
        return this;
    }
}