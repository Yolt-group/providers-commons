package com.yolt.providers.common.domain;

import com.yolt.providers.common.rest.ExternalRestTemplateBuilderFactory;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.security.cert.X509Certificate;
import java.util.UUID;
import java.util.function.Function;

public class RestTemplateManagerConfigurationBuilder {

    private final RestTemplateManagerConfiguration restTemplateManagerConfiguration;

    private RestTemplateManagerConfigurationBuilder(UUID privateTransportKid,
                                                    X509Certificate clientCertificate,
                                                    Function<ExternalRestTemplateBuilderFactory, RestTemplate> customizationFunction) {
        this.restTemplateManagerConfiguration = new RestTemplateManagerConfiguration(privateTransportKid, clientCertificate, customizationFunction);
    }
    private RestTemplateManagerConfigurationBuilder(UUID privateTransportKid,
                                                    X509Certificate[] certificateChain,
                                                    Function<ExternalRestTemplateBuilderFactory, RestTemplate> customizationFunction) {
        this.restTemplateManagerConfiguration = new RestTemplateManagerConfiguration(privateTransportKid, certificateChain, customizationFunction);
    }



    private RestTemplateManagerConfigurationBuilder(Function<ExternalRestTemplateBuilderFactory, RestTemplate> customizationFunction) {
        this.restTemplateManagerConfiguration = new RestTemplateManagerConfiguration(customizationFunction);
    }

    public static RestTemplateManagerConfigurationBuilder mutualTlsBuilder(@NotNull UUID privateTransportKid,
                                                                           @NotNull X509Certificate clientCertificate,
                                                                           @NotNull Function<ExternalRestTemplateBuilderFactory, RestTemplate> customizationFunction) {
        return new RestTemplateManagerConfigurationBuilder(privateTransportKid, clientCertificate, customizationFunction);
    }

    public static RestTemplateManagerConfigurationBuilder mutualTlsBuilder(@NotNull UUID privateTransportKid,
                                                                           @NotNull X509Certificate[] certificateChain,
                                                                           @NotNull Function<ExternalRestTemplateBuilderFactory, RestTemplate> customizationFunction) {
        return new RestTemplateManagerConfigurationBuilder(privateTransportKid, certificateChain, customizationFunction);
    }

    public static RestTemplateManagerConfigurationBuilder nonMutualTlsBuilder(@NotNull Function<ExternalRestTemplateBuilderFactory, RestTemplate> customizationFunction) {
        return new RestTemplateManagerConfigurationBuilder(customizationFunction);
    }

    public RestTemplateManagerConfigurationBuilder disableRedirectHandling() {
        this.restTemplateManagerConfiguration.setDisableRedirectHandling(true);
        return this;
    }

    public RestTemplateManagerConfigurationBuilder defaultKeepAliveTimeoutInMillis(long defaultKeepAliveTimeoutInMillis) {
        this.restTemplateManagerConfiguration.setDefaultKeepAliveTimeoutInMillis(defaultKeepAliveTimeoutInMillis);
        return this;
    }

    public RestTemplateManagerConfiguration build() {
        return this.restTemplateManagerConfiguration;
    }
}
