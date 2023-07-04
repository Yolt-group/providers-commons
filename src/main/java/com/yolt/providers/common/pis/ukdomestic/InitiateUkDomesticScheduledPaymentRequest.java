package com.yolt.providers.common.pis.ukdomestic;

import com.yolt.providers.common.cryptography.RestTemplateManager;
import com.yolt.providers.common.cryptography.Signer;
import com.yolt.providers.common.domain.authenticationmeans.BasicAuthenticationMean;
import lombok.Value;
import nl.ing.lovebird.providershared.api.AuthenticationMeansReference;
import org.springframework.lang.Nullable;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;


@Value
public class InitiateUkDomesticScheduledPaymentRequest {

    /**
     * Required payment data according to open-banking specifications.
     */
    @Valid
    @NotNull
    InitiateUkDomesticScheduledPaymentRequestDTO requestDTO;

    /**
     * This is the base url registered by a client of Yolt.  This url typically points to
     * a system that is in control of a client.
     *
     * When generating the url to redirect the user **to the bank** to acquire the users consent, this
     * field is typically included as the value for the "redirect_uri" query parameter (oAuth2).
     *
     * Example: "http://www.yolt.com/callback"
     */
    @Nullable
    String baseClientRedirectUrl;

    /**
     * The state which should be used when generating the authorize url.
     */
    @Nullable
    String state;

    /**
     * Collection of client authentication means for Yolt to be able to initiate a payment on client's behalf.
     */
    @NotEmpty
    Map<String, BasicAuthenticationMean> authenticationMeans;

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
