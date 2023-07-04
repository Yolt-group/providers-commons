package com.yolt.providers.common.ais.url;

import com.yolt.providers.common.cryptography.RestTemplateManager;
import com.yolt.providers.common.cryptography.Signer;
import com.yolt.providers.common.domain.authenticationmeans.BasicAuthenticationMean;
import com.yolt.providers.common.domain.autoonboarding.RegistrationOperation;
import lombok.NoArgsConstructor;
import nl.ing.lovebird.providerdomain.TokenScope;

import java.util.List;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
public class UrlAutoOnboardingRequestBuilder {

    private Map<String, BasicAuthenticationMean> authenticationMeans;
    private RestTemplateManager restTemplateManager;
    private Signer signer;
    private String baseClientRedirectUrl;
    private List<String> redirectUrls;
    private Set<TokenScope> scopes;
    private RegistrationOperation registrationOperation = RegistrationOperation.CREATE;

    public UrlAutoOnboardingRequest build() {
        return new UrlAutoOnboardingRequest(authenticationMeans, restTemplateManager, signer, registrationOperation, baseClientRedirectUrl, redirectUrls, scopes);
    }

    public UrlAutoOnboardingRequestBuilder setAuthenticationMeans(Map<String, BasicAuthenticationMean> authenticationMeans) {
        this.authenticationMeans = authenticationMeans;
        return this;
    }

    public UrlAutoOnboardingRequestBuilder setRestTemplateManager(RestTemplateManager restTemplateManager) {
        this.restTemplateManager = restTemplateManager;
        return this;
    }

    public UrlAutoOnboardingRequestBuilder setSigner(Signer signer) {
        this.signer = signer;
        return this;
    }

    public UrlAutoOnboardingRequestBuilder setBaseClientRedirectUrl(String baseClientRedirectUrl) {
        this.baseClientRedirectUrl = baseClientRedirectUrl;
        return this;
    }

    public UrlAutoOnboardingRequestBuilder setRedirectUrls(List<String> redirectUrls) {
        this.redirectUrls = redirectUrls;
        return this;
    }

    public UrlAutoOnboardingRequestBuilder setScopes(Set<TokenScope> scopes) {
        this.scopes = scopes;
        return this;
    }

    public UrlAutoOnboardingRequestBuilder setRegistrationOperation(RegistrationOperation registrationOperation) {
        this.registrationOperation = registrationOperation;
        return this;
    }
}