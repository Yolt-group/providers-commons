package com.yolt.providers.common.ais.url;

import com.yolt.providers.common.cryptography.RestTemplateManager;
import com.yolt.providers.common.cryptography.Signer;
import com.yolt.providers.common.domain.authenticationmeans.BasicAuthenticationMean;
import lombok.NoArgsConstructor;
import nl.ing.lovebird.providershared.AccessMeansDTO;
import nl.ing.lovebird.providershared.api.AuthenticationMeansReference;

import java.util.Map;

@NoArgsConstructor
public class UrlOnUserSiteDeleteRequestBuilder {

    private String externalConsentId;
    private AuthenticationMeansReference authenticationMeansReference;
    private Map<String, BasicAuthenticationMean> authenticationMeans;
    private AccessMeansDTO accessMeans;
    private Signer signer;
    private RestTemplateManager restTemplateManager;
    private String psuIpAddress;

    public UrlOnUserSiteDeleteRequest build() {
        return new UrlOnUserSiteDeleteRequest(externalConsentId, authenticationMeansReference, authenticationMeans, accessMeans, signer, restTemplateManager,
                psuIpAddress);
    }

    public UrlOnUserSiteDeleteRequestBuilder setExternalConsentId(final String externalConsentId) {
        this.externalConsentId = externalConsentId;
        return this;
    }

    public UrlOnUserSiteDeleteRequestBuilder setAuthenticationMeansReference(final AuthenticationMeansReference authenticationMeansReference) {
        this.authenticationMeansReference = authenticationMeansReference;
        return this;
    }

    public UrlOnUserSiteDeleteRequestBuilder setAuthenticationMeans(final Map<String, BasicAuthenticationMean> authenticationMeans) {
        this.authenticationMeans = authenticationMeans;
        return this;
    }

    public UrlOnUserSiteDeleteRequestBuilder setSigner(final Signer signer) {
        this.signer = signer;
        return this;
    }

    public UrlOnUserSiteDeleteRequestBuilder setRestTemplateManager(final RestTemplateManager restTemplateManager) {
        this.restTemplateManager = restTemplateManager;
        return this;
    }

    public UrlOnUserSiteDeleteRequestBuilder setPsuIpAddress(final String psuIpAddress) {
        this.psuIpAddress = psuIpAddress;
        return this;
    }

    public UrlOnUserSiteDeleteRequestBuilder setAccessMeans(final AccessMeansDTO accessMeansDTO) {
        this.accessMeans = accessMeansDTO;
        return this;
    }
}