package com.yolt.providers.common.domain;

import com.yolt.providers.common.rest.ExternalRestTemplateBuilderFactory;
import lombok.Data;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.security.cert.X509Certificate;
import java.util.UUID;
import java.util.function.Function;

@Data
public class RestTemplateManagerConfiguration {
    /**
     * Identifier of a transport certificate to get to the HSM for a private key - required for setting up mutual TLS connection.
     * For mutual TLS must be provided along with clientCertificate.
     */
    private UUID privateTransportKid;

    /**
     * Client transport certificate chain - required for setting up mutual TLS connection.
     * For mutual TLS must be provided along with privateTransportKid.
     */

    private X509Certificate[] clientCertificateChain;

    /**
     * The required "customizationFunction" receives a preconfigured {@link ExternalRestTemplateBuilderFactory} with this configured:
     * - truststore
     * - client key managers
     * - cipher suites
     * - trust on first use certificate pinner
     * - http(s) proxies
     * - standard message converters
     */
    @NotNull
    private Function<ExternalRestTemplateBuilderFactory, RestTemplate> customizationFunction;

    /**
     * Optional field which takes control of enabling or disabling redirect handling on Http Client level
     */
    private boolean disableRedirectHandling;

    /**
     * Optional field which takes control over Persistent Connection timeout in millis
     * in case Keep-Alive HTTP header is not provided by bank in response
     */
    private long defaultKeepAliveTimeoutInMillis;

    public RestTemplateManagerConfiguration(@NotNull UUID privateTransportKid,
                                            @NotNull X509Certificate[] clientCertificateChain,
                                            @NotNull Function<ExternalRestTemplateBuilderFactory, RestTemplate> customizationFunction) {
        this.privateTransportKid = privateTransportKid;
        this.clientCertificateChain = clientCertificateChain;
        this.customizationFunction = customizationFunction;
    }

    /**
     * Creates an instance of a configuration class with required fields needed to setup proper basic configuration of Rest Template with mTLS.
     *
     * @param privateTransportKid
     * @param clientCertificate
     * @param customizationFunction
     */
    public RestTemplateManagerConfiguration(@NotNull UUID privateTransportKid,
                                            @NotNull X509Certificate clientCertificate,
                                            @NotNull Function<ExternalRestTemplateBuilderFactory, RestTemplate> customizationFunction) {
        this.privateTransportKid = privateTransportKid;
        this.clientCertificateChain = new X509Certificate[] {clientCertificate};
        this.customizationFunction = customizationFunction;
    }

    public RestTemplateManagerConfiguration(@NotNull Function<ExternalRestTemplateBuilderFactory, RestTemplate> customizationFunction) {
        this.customizationFunction = customizationFunction;
    }

    public boolean isMutualTlsConfiguration() {
        return this.privateTransportKid != null && this.clientCertificateChain != null;
    }

}
