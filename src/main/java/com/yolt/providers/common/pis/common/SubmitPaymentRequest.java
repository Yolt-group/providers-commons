package com.yolt.providers.common.pis.common;

import com.yolt.providers.common.cryptography.RestTemplateManager;
import com.yolt.providers.common.cryptography.Signer;
import com.yolt.providers.common.domain.authenticationmeans.BasicAuthenticationMean;
import com.yolt.providers.common.providerinterface.UkDomesticPaymentProvider;
import lombok.Value;
import nl.ing.lovebird.providershared.api.AuthenticationMeansReference;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Value
public class SubmitPaymentRequest {

    /**
     * Optional data that was returned on initiate payment endpoint (usually in form of JSON).
     * Check {@link UkDomesticPaymentProvider} for more details.
     */
    @Nullable
    String providerState;

    /**
     * Collection of client authentication means for Yolt to be able to initiate a payment on client's behalf.
     */
    @NotEmpty
    Map<String, BasicAuthenticationMean> authenticationMeans;

    /**
     * This is the URL to which the user was redirected *by the bank* after giving their consent.
     *
     * After a user has given their consent, the bank redirects the user to baseClientRedirectUrl with some
     * additional parameters added by the bank, one of which is typically "code", the value of which can be
     * exchanged for a token at the bank (oAuth2).
     *
     * Example: "http://www.yolt.com/callback?authorization_code=<some-state-added-by-the-bank/>&state=<some-state-generated-by-Yolt/>"
     */
    @NotNull
    String redirectUrlPostedBackFromSite;

    /**
     * Bank specific signing service (currently either HTTPS signature or JWS signature are supported)
     */
    @NotNull
    Signer signer;

    /**
     * See {@link RestTemplateManager} documentation.
     */
    @NotNull
    RestTemplateManager restTemplateManager;

    /**
     * The ipv4 or ipv6 address of the payment service user performing an action.
     */
    @Nullable
    String psuIpAddress;

    @NotNull
    AuthenticationMeansReference authenticationMeansReference;
}
