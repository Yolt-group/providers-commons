package com.yolt.providers.common.cryptography;

import com.yolt.providers.common.domain.RestTemplateManagerConfiguration;
import com.yolt.providers.common.rest.ExternalRestTemplateBuilderFactory;
import org.springframework.web.client.RestTemplate;

import java.security.cert.X509Certificate;
import java.util.UUID;
import java.util.function.Function;

/**
 * In practive we have separate instances of {@link RestTemplateManager} for every combination of { PROVIDER, CLIENT, SERVICE_TYPE }.
 * The collection of {@link RestTemplateManager} classes are managed by "MutualTLSRestTemplateManagerCache" in the `providers` project.
 * <p>
 * The responsibility of this class is to cache {@link RestTemplate}'s because setting up (m)TLS connections is expensive.
 */
public interface RestTemplateManager {
    /**
     * Callers of this method are *not* responsible for setting up the {@link RestTemplate}.
     * This method is unsupported by default because it is only used by a small group of providers, which requires a client certificate chain in mutual TLS communication.
     * Internal flow is exactly the same, as is used by manage method with client certificate.
     * The "customizationFunction" receives a preconfigured {@link ExternalRestTemplateBuilderFactory} with this configured:
     * - truststore
     * - client key managers
     * - cipher suites
     * - trust on first use certificate pinner
     * - http(s) proxies
     * - standard message converters
     */
    RestTemplate manage(UUID privateTransportKid,
                        X509Certificate[] clientCertificateChain,
                        Function<ExternalRestTemplateBuilderFactory, RestTemplate> customizationFunction);

    /**
     * This method allows to build Rest Template with an additional configuration if needed
     * Callers of this method are responsible for setting up the {@link RestTemplate} themselves from scratch, including:
     * - truststore
     * - client key managers
     * - cipher suites
     * - certificate pinning
     * - http(s) proxies
     */
    RestTemplate manage(RestTemplateManagerConfiguration restTemplateManagerConfiguration);
}
