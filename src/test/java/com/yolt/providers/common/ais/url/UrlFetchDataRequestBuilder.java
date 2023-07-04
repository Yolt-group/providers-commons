package com.yolt.providers.common.ais.url;

import com.yolt.providers.common.cryptography.RestTemplateManager;
import com.yolt.providers.common.cryptography.Signer;
import com.yolt.providers.common.domain.authenticationmeans.BasicAuthenticationMean;
import lombok.NoArgsConstructor;
import nl.ing.lovebird.providershared.AccessMeansDTO;
import nl.ing.lovebird.providershared.api.AuthenticationMeansReference;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@NoArgsConstructor
public class UrlFetchDataRequestBuilder {

    private UUID userId;
    private UUID userSiteId;
    private Instant transactionsFetchStartTime;
    private AccessMeansDTO accessMeans;
    private Map<String, BasicAuthenticationMean> authenticationMeans;
    private Signer signer;
    private RestTemplateManager restTemplateManager;
    private AuthenticationMeansReference authenticationMeansReference;
    private String psuIpAddress;

    public UrlFetchDataRequest build() {
        return new UrlFetchDataRequest(userId, userSiteId, transactionsFetchStartTime, accessMeans, authenticationMeans, signer,
                restTemplateManager, authenticationMeansReference, psuIpAddress);
    }

    public UrlFetchDataRequestBuilder setUserId(final UUID userId) {
        this.userId = userId;
        return this;
    }

    public UrlFetchDataRequestBuilder setUserSiteId(final UUID userSiteId) {
        this.userSiteId = userSiteId;
        return this;
    }

    public UrlFetchDataRequestBuilder setTransactionsFetchStartTime(final Instant transactionsFetchStartTime) {
        this.transactionsFetchStartTime = transactionsFetchStartTime;
        return this;
    }

    public UrlFetchDataRequestBuilder setAccessMeans(final AccessMeansDTO accessMeans) {
        this.accessMeans = accessMeans;
        return this;
    }

    public UrlFetchDataRequestBuilder setAccessMeans(final UUID userId, final String accessMeans, final Date updated, final Date expireTime) {
        this.accessMeans = new AccessMeansDTO(userId, accessMeans, updated, expireTime);
        return this;
    }

    public UrlFetchDataRequestBuilder setAuthenticationMeans(final Map<String, BasicAuthenticationMean> authenticationMeans) {
        this.authenticationMeans = authenticationMeans;
        return this;
    }

    public UrlFetchDataRequestBuilder setSigner(final Signer signer) {
        this.signer = signer;
        return this;
    }

    public UrlFetchDataRequestBuilder setRestTemplateManager(final RestTemplateManager restTemplateManager) {
        this.restTemplateManager = restTemplateManager;
        return this;
    }

    public UrlFetchDataRequestBuilder setAuthenticationMeansReference(final AuthenticationMeansReference authenticationMeansReference) {
        this.authenticationMeansReference = authenticationMeansReference;
        return this;
    }

    public UrlFetchDataRequestBuilder setPsuIpAddress(final String psuIpAddress) {
        this.psuIpAddress = psuIpAddress;
        return this;
    }
}