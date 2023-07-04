package com.yolt.providers.common.ais.url;

import com.yolt.providers.common.cryptography.RestTemplateManager;
import com.yolt.providers.common.cryptography.Signer;
import com.yolt.providers.common.domain.authenticationmeans.BasicAuthenticationMean;
import lombok.AllArgsConstructor;
import lombok.Value;
import nl.ing.lovebird.providershared.form.FilledInUserSiteFormValues;
import org.springframework.lang.Nullable;

import java.util.Map;
import java.util.UUID;

@Value
@AllArgsConstructor
public class UrlCreateAccessMeansRequest {

    UUID userId;


    /**
     * This is the URL to which the user was redirected *by the bank* after giving their consent.
     *
     * After a user has given their consent, the bank redirects the user to baseClientRedirectUrl with some
     * additional parameters added by the bank, one of which is typically "code", the value of which can be
     * exchanged for a token at the bank (oAuth2).
     *
     * This is **EMPTY** if the user has submitted a form (when {@link #filledInUserSiteFormValues} is filled).
     *
     * Example: "http://www.yolt.com/callback?authorization_code=<some-state-added-by-the-bank/>&state=<some-state-generated-by-Yolt/>"
     */
    String redirectUrlPostedBackFromSite;

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

    Map<String, BasicAuthenticationMean> authenticationMeans;

    /**
     * Can be used to store state a provider needs so that it is preserved between subsequent steps.
     */
    String providerState;
    Signer signer;
    RestTemplateManager restTemplateManager;

    /**
     * If this is non-null, then {@link #redirectUrlPostedBackFromSite} **IS NULL**.
     */
    FilledInUserSiteFormValues filledInUserSiteFormValues;

    /**
     * When generating a url **to the bank** to acquire the users consent, this field is typically
     * included as the value for the "state" query parameter (oAuth2).
     */
    String state;

    /**
     * The ipv4 or ipv6 address of the payment service user performing an action.
     */
    @Nullable
    String psuIpAddress;

}