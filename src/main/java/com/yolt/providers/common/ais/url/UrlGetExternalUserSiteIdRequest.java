package com.yolt.providers.common.ais.url;

import com.yolt.providers.common.cryptography.RestTemplateManager;
import com.yolt.providers.common.cryptography.Signer;
import com.yolt.providers.common.domain.authenticationmeans.BasicAuthenticationMean;
import lombok.AllArgsConstructor;
import lombok.Value;
import nl.ing.lovebird.providershared.AccessMeansDTO;
import org.springframework.lang.Nullable;

import java.util.Map;

@Value
@AllArgsConstructor
public class UrlGetExternalUserSiteIdRequest {

    AccessMeansDTO accessMeans;
    Map<String, BasicAuthenticationMean> authenticationMeans;
    Signer signer;
    RestTemplateManager restTemplateManager;
    /**
     * The ipv4 or ipv6 address of the payment service user performing an action.
     */
    @Nullable
    String psuIpAddress;

}