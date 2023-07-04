package com.yolt.providers.common.pis.common;

import com.yolt.providers.common.cryptography.RestTemplateManager;
import com.yolt.providers.common.cryptography.Signer;
import com.yolt.providers.common.domain.authenticationmeans.BasicAuthenticationMean;
import lombok.Value;
import nl.ing.lovebird.providershared.api.AuthenticationMeansReference;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Value
public class GetStatusRequest {

    @Nullable
    String providerState;

    @Nullable
    String paymentId;
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
