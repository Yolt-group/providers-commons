package com.yolt.providers.common.pis.sepa;

import com.yolt.providers.common.cryptography.RestTemplateManager;
import com.yolt.providers.common.cryptography.Signer;
import com.yolt.providers.common.domain.authenticationmeans.BasicAuthenticationMean;
import lombok.NoArgsConstructor;
import nl.ing.lovebird.providershared.api.AuthenticationMeansReference;

import java.util.Map;

@NoArgsConstructor
public class InitiatePaymentRequestBuilder {

    private SepaInitiatePaymentRequestDTO requestDTO;
    private String baseClientRedirectUrl;
    private String state;
    private Map<String, BasicAuthenticationMean> authenticationMeans;
    private Signer signer;
    private RestTemplateManager restTemplateManager;
    private String psuIpAddress;
    private AuthenticationMeansReference authenticationMeansReference;

    public InitiatePaymentRequest build() {
        return new InitiatePaymentRequest(requestDTO, baseClientRedirectUrl, state, authenticationMeans, signer, restTemplateManager,
                psuIpAddress, authenticationMeansReference);
    }

    public InitiatePaymentRequestBuilder setRequestDTO(final SepaInitiatePaymentRequestDTO requestDTO) {
        this.requestDTO = requestDTO;
        return this;
    }

    public InitiatePaymentRequestBuilder setBaseClientRedirectUrl(final String baseClientRedirectUrl) {
        this.baseClientRedirectUrl = baseClientRedirectUrl;
        return this;
    }

    public InitiatePaymentRequestBuilder setState(final String state) {
        this.state = state;
        return this;
    }

    public InitiatePaymentRequestBuilder setAuthenticationMeans(final Map<String, BasicAuthenticationMean> authenticationMeans) {
        this.authenticationMeans = authenticationMeans;
        return this;
    }

    public InitiatePaymentRequestBuilder setSigner(final Signer signer) {
        this.signer = signer;
        return this;
    }

    public InitiatePaymentRequestBuilder setRestTemplateManager(final RestTemplateManager restTemplateManager) {
        this.restTemplateManager = restTemplateManager;
        return this;
    }

    public InitiatePaymentRequestBuilder setPsuIpAddress(final String psuIpAddress) {
        this.psuIpAddress = psuIpAddress;
        return this;
    }

    public InitiatePaymentRequestBuilder setAuthenticationMeansReference(AuthenticationMeansReference authenticationMeansReference) {
        this.authenticationMeansReference = authenticationMeansReference;
        return this;
    }
}
