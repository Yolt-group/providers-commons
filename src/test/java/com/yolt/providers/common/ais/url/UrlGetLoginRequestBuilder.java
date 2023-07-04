package com.yolt.providers.common.ais.url;

import com.yolt.providers.common.cryptography.RestTemplateManager;
import com.yolt.providers.common.cryptography.Signer;
import com.yolt.providers.common.domain.authenticationmeans.BasicAuthenticationMean;
import lombok.NoArgsConstructor;
import nl.ing.lovebird.providershared.api.AuthenticationMeansReference;

import java.util.Map;

@NoArgsConstructor
public class UrlGetLoginRequestBuilder {

    private String baseClientRedirectUrl;
    private String state;
    private AuthenticationMeansReference authenticationMeansReference;
    private Map<String, BasicAuthenticationMean> authenticationMeans;
    private String externalConsentId;
    private Signer signer;
    private RestTemplateManager restTemplateManager;
    private String psuIpAddress;

    public UrlGetLoginRequest build() {
        return new UrlGetLoginRequest(baseClientRedirectUrl, state, authenticationMeansReference, authenticationMeans, externalConsentId,
                signer, restTemplateManager, psuIpAddress);
    }

    public UrlGetLoginRequestBuilder setBaseClientRedirectUrl(final String baseClientRedirectUrl) {
        this.baseClientRedirectUrl = baseClientRedirectUrl;
        return this;
    }

    public UrlGetLoginRequestBuilder setState(final String state) {
        this.state = state;
        return this;
    }

    public UrlGetLoginRequestBuilder setAuthenticationMeansReference(final AuthenticationMeansReference authenticationMeansReference) {
        this.authenticationMeansReference = authenticationMeansReference;
        return this;
    }

    public UrlGetLoginRequestBuilder setAuthenticationMeans(final Map<String, BasicAuthenticationMean> authenticationMeans) {
        this.authenticationMeans = authenticationMeans;
        return this;
    }

    public UrlGetLoginRequestBuilder setExternalConsentId(final String externalConsentId) {
        this.externalConsentId = externalConsentId;
        return this;
    }

    public UrlGetLoginRequestBuilder setSigner(final Signer signer) {
        this.signer = signer;
        return this;
    }

    public UrlGetLoginRequestBuilder setRestTemplateManager(final RestTemplateManager restTemplateManager) {
        this.restTemplateManager = restTemplateManager;
        return this;
    }

    public UrlGetLoginRequestBuilder setPsuIpAddress(final String psuIpAddress) {
        this.psuIpAddress = psuIpAddress;
        return this;
    }
}