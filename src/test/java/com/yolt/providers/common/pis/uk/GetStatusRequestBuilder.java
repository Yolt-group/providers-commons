package com.yolt.providers.common.pis.uk;

import com.yolt.providers.common.cryptography.RestTemplateManager;
import com.yolt.providers.common.cryptography.Signer;
import com.yolt.providers.common.domain.authenticationmeans.BasicAuthenticationMean;
import com.yolt.providers.common.pis.common.GetStatusRequest;
import lombok.NoArgsConstructor;
import nl.ing.lovebird.providershared.api.AuthenticationMeansReference;

import java.util.Map;

@NoArgsConstructor
public class GetStatusRequestBuilder {

    private String providerState;
    private String paymentId;
    private Map<String, BasicAuthenticationMean> authenticationMeans;
    private Signer signer;
    private RestTemplateManager restTemplateManager;
    private String psuIpAddress;
    private AuthenticationMeansReference authenticationMeansReference;

    public GetStatusRequest build() {
        return new GetStatusRequest(providerState,
                paymentId,
                authenticationMeans,
                signer,
                restTemplateManager,
                psuIpAddress,
                authenticationMeansReference);
    }

    public GetStatusRequestBuilder setProviderState(String providerState) {
        this.providerState = providerState;
        return this;
    }

    public GetStatusRequestBuilder setPaymentId(String paymentId) {
        this.paymentId = paymentId;
        return this;
    }

    public GetStatusRequestBuilder setAuthenticationMeans(Map<String, BasicAuthenticationMean> authenticationMeans) {
        this.authenticationMeans = authenticationMeans;
        return this;
    }

    public GetStatusRequestBuilder setSigner(Signer signer) {
        this.signer = signer;
        return this;
    }

    public GetStatusRequestBuilder setRestTemplateManager(RestTemplateManager restTemplateManager) {
        this.restTemplateManager = restTemplateManager;
        return this;
    }

    public GetStatusRequestBuilder setPsuIpAddress(String psuIpAddress) {
        this.psuIpAddress = psuIpAddress;
        return this;
    }

    public GetStatusRequestBuilder setAuthenticationMeansReference(AuthenticationMeansReference authenticationMeansReference) {
        this.authenticationMeansReference = authenticationMeansReference;
        return this;
    }
}
