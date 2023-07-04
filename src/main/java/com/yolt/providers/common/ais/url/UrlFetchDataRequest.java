package com.yolt.providers.common.ais.url;

import com.yolt.providers.common.cryptography.RestTemplateManager;
import com.yolt.providers.common.cryptography.Signer;
import com.yolt.providers.common.domain.authenticationmeans.BasicAuthenticationMean;
import lombok.AllArgsConstructor;
import lombok.Value;
import nl.ing.lovebird.providershared.AccessMeansDTO;
import nl.ing.lovebird.providershared.api.AuthenticationMeansReference;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Value
@AllArgsConstructor
public class UrlFetchDataRequest {
    UUID userId;
    UUID userSiteId;
    Instant transactionsFetchStartTime;
    AccessMeansDTO accessMeans;
    Map<String, BasicAuthenticationMean> authenticationMeans;
    Signer signer;
    RestTemplateManager restTemplateManager;
    AuthenticationMeansReference authenticationMeansReference;
    /**
     * The ipv4 or ipv6 address of the payment service user performing an action.
     */
    @Nullable
    String psuIpAddress;
}