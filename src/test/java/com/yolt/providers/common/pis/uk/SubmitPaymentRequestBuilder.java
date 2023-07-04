package com.yolt.providers.common.pis.uk;

import com.yolt.providers.common.cryptography.RestTemplateManager;
import com.yolt.providers.common.cryptography.Signer;
import com.yolt.providers.common.domain.authenticationmeans.BasicAuthenticationMean;
import com.yolt.providers.common.pis.common.SubmitPaymentRequest;
import lombok.NoArgsConstructor;
import nl.ing.lovebird.providershared.api.AuthenticationMeansReference;

import java.util.Map;

@NoArgsConstructor
public class SubmitPaymentRequestBuilder {

    private String providerState;
    private Map<String, BasicAuthenticationMean> authenticationMeans;
    private String redirectUrlPostedBackFromSite;
    private Signer signer;
    private RestTemplateManager restTemplateManager;
    private String psuIpAddress;
    private AuthenticationMeansReference authenticationMeansReference;

    public SubmitPaymentRequest build() {
        return new SubmitPaymentRequest(providerState, authenticationMeans, redirectUrlPostedBackFromSite, signer, restTemplateManager,
                psuIpAddress, authenticationMeansReference);
    }

    public SubmitPaymentRequestBuilder setProviderState(final String providerState) {
        this.providerState = providerState;
        return this;
    }

    public SubmitPaymentRequestBuilder setAuthenticationMeans(final Map<String, BasicAuthenticationMean> authenticationMeans) {
        this.authenticationMeans = authenticationMeans;
        return this;
    }

    public SubmitPaymentRequestBuilder setRedirectUrlPostedBackFromSite(final String redirectUrlPostedBackFromSite) {
        this.redirectUrlPostedBackFromSite = redirectUrlPostedBackFromSite;
        return this;
    }

    public SubmitPaymentRequestBuilder setSigner(final Signer signer) {
        this.signer = signer;
        return this;
    }

    public SubmitPaymentRequestBuilder setRestTemplateManager(final RestTemplateManager restTemplateManager) {
        this.restTemplateManager = restTemplateManager;
        return this;
    }

    public SubmitPaymentRequestBuilder setPsuIpAddress(final String psuIpAddress) {
        this.psuIpAddress = psuIpAddress;
        return this;
    }

    public SubmitPaymentRequestBuilder setAuthenticationMeansReference(AuthenticationMeansReference authenticationMeansReference) {
        this.authenticationMeansReference = authenticationMeansReference;
        return this;
    }
}
