package com.yolt.providers.common.ais.url;

import com.yolt.providers.common.cryptography.RestTemplateManager;
import com.yolt.providers.common.cryptography.Signer;
import com.yolt.providers.common.domain.authenticationmeans.BasicAuthenticationMean;
import lombok.AllArgsConstructor;
import lombok.Value;
import nl.ing.lovebird.providershared.AccessMeansDTO;
import nl.ing.lovebird.providershared.api.AuthenticationMeansReference;
import org.springframework.lang.Nullable;

import java.util.Map;

@Value
@AllArgsConstructor
public class UrlOnUserSiteDeleteRequest {

    /**
     * The consent ID which is known at the site. This cannot be null, since then it wouldn't make sense to send a notification..
     */
    String externalConsentId;

    AuthenticationMeansReference authenticationMeansReference;
    Map<String, BasicAuthenticationMean> authenticationMeans;
    AccessMeansDTO accessMeans;
    Signer signer;
    RestTemplateManager restTemplateManager;
    /**
     * The ipv4 or ipv6 address of the payment service user performing an action.
     */
    @Nullable
    String psuIpAddress;

}
