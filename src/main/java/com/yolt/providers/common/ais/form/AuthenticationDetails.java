package com.yolt.providers.common.ais.form;

import com.yolt.providers.common.cryptography.RestTemplateManager;
import com.yolt.providers.common.cryptography.Signer;
import com.yolt.providers.common.domain.authenticationmeans.BasicAuthenticationMean;
import lombok.Value;

import java.util.Map;

@Value
public class AuthenticationDetails {

    private final Signer signer;
    private final RestTemplateManager restTemplateManager;
    private final Map<String, BasicAuthenticationMean> authenticationMeans;

}
