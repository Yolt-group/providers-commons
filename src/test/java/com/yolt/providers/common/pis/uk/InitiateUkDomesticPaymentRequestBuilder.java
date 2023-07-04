package com.yolt.providers.common.pis.uk;

import com.yolt.providers.common.cryptography.RestTemplateManager;
import com.yolt.providers.common.cryptography.Signer;
import com.yolt.providers.common.domain.authenticationmeans.BasicAuthenticationMean;
import com.yolt.providers.common.pis.ukdomestic.InitiateUkDomesticPaymentRequest;
import com.yolt.providers.common.pis.ukdomestic.InitiateUkDomesticPaymentRequestDTO;
import lombok.NoArgsConstructor;
import nl.ing.lovebird.providershared.api.AuthenticationMeansReference;

import java.util.Map;

@NoArgsConstructor
public class InitiateUkDomesticPaymentRequestBuilder {

    private InitiateUkDomesticPaymentRequestDTO requestDTO;
    private String baseClientRedirectUrl;
    private String state;
    private Map<String, BasicAuthenticationMean> authenticationMeans;
    private Signer signer;
    private RestTemplateManager restTemplateManager;
    private String psuIpAddress;
    private AuthenticationMeansReference authenticationMeansReference;

    public InitiateUkDomesticPaymentRequest build() {
        return new InitiateUkDomesticPaymentRequest(requestDTO,
                baseClientRedirectUrl,
                state,
                authenticationMeans,
                signer,
                restTemplateManager,
                psuIpAddress,
                authenticationMeansReference);
    }

    public InitiateUkDomesticPaymentRequestBuilder setRequestDTO(InitiateUkDomesticPaymentRequestDTO requestDTO) {
        this.requestDTO = requestDTO;
        return this;
    }

    public InitiateUkDomesticPaymentRequestBuilder setBaseClientRedirectUrl(String baseClientRedirectUrl) {
        this.baseClientRedirectUrl = baseClientRedirectUrl;
        return this;
    }

    public InitiateUkDomesticPaymentRequestBuilder setState(String state) {
        this.state = state;
        return this;
    }

    public InitiateUkDomesticPaymentRequestBuilder setAuthenticationMeans(Map<String, BasicAuthenticationMean> authenticationMeans) {
        this.authenticationMeans = authenticationMeans;
        return this;
    }

    public InitiateUkDomesticPaymentRequestBuilder setSigner(Signer signer) {
        this.signer = signer;
        return this;
    }

    public InitiateUkDomesticPaymentRequestBuilder setRestTemplateManager(RestTemplateManager restTemplateManager) {
        this.restTemplateManager = restTemplateManager;
        return this;
    }

    public InitiateUkDomesticPaymentRequestBuilder setPsuIpAddress(String psuIpAddress) {
        this.psuIpAddress = psuIpAddress;
        return this;
    }

    public InitiateUkDomesticPaymentRequestBuilder setAuthenticationMeansReference(AuthenticationMeansReference authenticationMeansReference) {
        this.authenticationMeansReference = authenticationMeansReference;
        return this;
    }
}
