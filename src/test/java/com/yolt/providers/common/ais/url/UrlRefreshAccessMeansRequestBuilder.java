package com.yolt.providers.common.ais.url;

import com.yolt.providers.common.cryptography.RestTemplateManager;
import com.yolt.providers.common.cryptography.Signer;
import com.yolt.providers.common.domain.authenticationmeans.BasicAuthenticationMean;
import lombok.NoArgsConstructor;
import nl.ing.lovebird.providershared.AccessMeansDTO;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@NoArgsConstructor
public class UrlRefreshAccessMeansRequestBuilder {

    private AccessMeansDTO accessMeans;
    private Map<String, BasicAuthenticationMean> authenticationMeans;
    private Signer signer;
    private RestTemplateManager restTemplateManager;
    private String psuIpAddress;

    public UrlRefreshAccessMeansRequest build() {
        return new UrlRefreshAccessMeansRequest(accessMeans, authenticationMeans, signer, restTemplateManager, psuIpAddress);
    }

    public UrlRefreshAccessMeansRequestBuilder setAccessMeans(final AccessMeansDTO accessMeans) {
        this.accessMeans = accessMeans;
        return this;
    }

    public UrlRefreshAccessMeansRequestBuilder setAccessMeans(final UUID userId, final String accessMeans, final Date updated, final Date expireTime) {
        this.accessMeans = new AccessMeansDTO(userId, accessMeans, updated, expireTime);
        return this;
    }

    public UrlRefreshAccessMeansRequestBuilder setAuthenticationMeans(final Map<String, BasicAuthenticationMean> authenticationMeans) {
        this.authenticationMeans = authenticationMeans;
        return this;
    }

    public UrlRefreshAccessMeansRequestBuilder setSigner(final Signer signer) {
        this.signer = signer;
        return this;
    }

    public UrlRefreshAccessMeansRequestBuilder setRestTemplateManager(final RestTemplateManager restTemplateManager) {
        this.restTemplateManager = restTemplateManager;
        return this;
    }

    public UrlRefreshAccessMeansRequestBuilder setPsuIpAddress(final String psuIpAddress) {
        this.psuIpAddress = psuIpAddress;
        return this;
    }
}