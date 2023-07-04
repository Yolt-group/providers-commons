package com.yolt.providers.common.ais.url;

import com.yolt.providers.common.cryptography.RestTemplateManager;
import com.yolt.providers.common.cryptography.Signer;
import com.yolt.providers.common.domain.authenticationmeans.BasicAuthenticationMean;
import com.yolt.providers.common.domain.autoonboarding.RegistrationOperation;
import lombok.AllArgsConstructor;
import lombok.Value;
import nl.ing.lovebird.providerdomain.TokenScope;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Value
@AllArgsConstructor
public class UrlAutoOnboardingRequest {

    Map<String, BasicAuthenticationMean> authenticationMeans;
    RestTemplateManager restTemplateManager;
    Signer signer;
    RegistrationOperation registrationOperation;


    /**
     * The bank sometimes needs to "register" or "know" which values for "redirect_uri" a client will use in the token flow (oAuth2).
     */
    // TODO Remove it after migration of all banks to new autoonboarding logic (multiple redirects and scopes). C4PO-8272
    @Deprecated
    String baseClientRedirectUrl;

    List<String> redirectUrls;

    Set<TokenScope> scopes;

    // TODO Remove it after migration of all banks to new autoonboarding logic (multiple redirects and scopes). C4PO-8272
    @Deprecated
    public UrlAutoOnboardingRequest(Map<String, BasicAuthenticationMean> authenticationMeans,
                                    RestTemplateManager restTemplateManager,
                                    Signer signer,
                                    String baseClientRedirectUrl) {
        this.authenticationMeans = authenticationMeans;
        this.restTemplateManager = restTemplateManager;
        this.signer = signer;
        this.baseClientRedirectUrl = baseClientRedirectUrl;
        this.registrationOperation = RegistrationOperation.CREATE;
        this.redirectUrls = Collections.singletonList(baseClientRedirectUrl);
        this.scopes = Collections.emptySet();
    }

    // TODO Remove it after migration of all banks to new autoonboarding logic (multiple redirects and scopes). C4PO-8272
    @Deprecated
    public UrlAutoOnboardingRequest(Map<String, BasicAuthenticationMean> authenticationMeans,
                                    RestTemplateManager restTemplateManager,
                                    Signer signer,
                                    RegistrationOperation registrationOperation,
                                    String baseClientRedirectUrl) {
        this.authenticationMeans = authenticationMeans;
        this.restTemplateManager = restTemplateManager;
        this.signer = signer;
        this.registrationOperation = registrationOperation;
        this.baseClientRedirectUrl = baseClientRedirectUrl;
        this.redirectUrls = Collections.singletonList(baseClientRedirectUrl);
        this.scopes = Collections.emptySet();
    }

    public UrlAutoOnboardingRequest(Map<String, BasicAuthenticationMean> authenticationMeans,
                                    RestTemplateManager restTemplateManager,
                                    Signer signer,
                                    String baseClientRedirectUrl,
                                    List<String> redirectUrls,
                                    Set<TokenScope> scopes) {
        this.authenticationMeans = authenticationMeans;
        this.restTemplateManager = restTemplateManager;
        this.signer = signer;
        this.registrationOperation = RegistrationOperation.CREATE;
        this.baseClientRedirectUrl = baseClientRedirectUrl;
        this.redirectUrls = redirectUrls;
        this.scopes = scopes;
    }

    public UrlAutoOnboardingRequest(Map<String, BasicAuthenticationMean> authenticationMeans,
                                    RestTemplateManager restTemplateManager,
                                    Signer signer,
                                    List<String> redirectUrls,
                                    Set<TokenScope> scopes) {
        this.authenticationMeans = authenticationMeans;
        this.restTemplateManager = restTemplateManager;
        this.signer = signer;
        this.registrationOperation = RegistrationOperation.CREATE;
        this.baseClientRedirectUrl = redirectUrls.get(0);
        this.redirectUrls = redirectUrls;
        this.scopes = scopes;
    }
}
