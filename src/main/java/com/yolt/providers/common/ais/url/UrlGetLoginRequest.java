package com.yolt.providers.common.ais.url;

import com.yolt.providers.common.cryptography.RestTemplateManager;
import com.yolt.providers.common.cryptography.Signer;
import com.yolt.providers.common.domain.authenticationmeans.BasicAuthenticationMean;
import com.yolt.providers.common.providerinterface.UrlDataProvider;
import lombok.AllArgsConstructor;
import lombok.Value;
import nl.ing.lovebird.providershared.api.AuthenticationMeansReference;
import org.springframework.lang.Nullable;

import java.util.Map;

/**
 * For {@link UrlDataProvider#getLoginInfo(UrlGetLoginRequest)}; passes arguments needed to compose a
 * authorization url for target provider.
 */
@Value
@AllArgsConstructor
public class UrlGetLoginRequest {

    /**
     * This is the base url registered by a client of Yolt.  This url typically points to
     * a system that is in control of a client.
     *
     * When generating the url to redirect the user **to the bank** to acquire the users consent, this
     * field is typically included as the value for the "redirect_uri" query parameter (oAuth2).
     *
     * Example: "http://www.yolt.com/callback"
     */
    String baseClientRedirectUrl;

    /**
     * When generating a url **to the bank** to acquire the users consent, this field is typically
     * included as the value for the "state" query parameter (oAuth2).
     */
    String state;

    AuthenticationMeansReference authenticationMeansReference;
    Map<String, BasicAuthenticationMean> authenticationMeans;

    /**
     * An optional consent ID which is known at the site. This will always be null if the login URL is for an add bank.
     */
    @Nullable
    String externalConsentId;

    Signer signer;
    RestTemplateManager restTemplateManager;

    /**
     * The ipv4 or ipv6 address of the payment service user performing an action.
     */
    @Nullable
    String psuIpAddress;

}