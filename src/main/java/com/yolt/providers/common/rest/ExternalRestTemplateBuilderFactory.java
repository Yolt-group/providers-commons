package com.yolt.providers.common.rest;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.yolt.providers.common.domain.logging.RawDataCensoringRule;
import com.yolt.providers.common.rest.intercepting.InterceptorSortingRestTemplateCustomizer;
import com.yolt.providers.common.rest.logging.LoggingInterceptor;
import com.yolt.providers.common.rest.monitoring.ResponseCodeLoggingInterceptor;
import com.yolt.providers.common.rest.tracing.ExternalTracingClientHttpRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplateHandler;

import java.time.Duration;
import java.util.Collection;
import java.util.Set;
import java.util.function.Supplier;

@Component
@Scope("prototype")
public class ExternalRestTemplateBuilderFactory {

    private static final String SENSITIVE_DATA_REPLACEMENT = "********";

    private RestTemplateBuilder restTemplateBuilder;
    private Collection<RawDataCensoringRule> censoringRules;
    @Value("${yolt.providers.rddmask.blacklisted-terms:}#{T(java.util.Collections).emptyList()}")
    private Set<String> blacklistedTerms;
    @Value("${yolt.providers.rddmask.max-rdd-body-to-be-masked-in-bytes:1000}")
    private int maxRddBodyToBeMaskedInBytes;

    public ExternalRestTemplateBuilderFactory() {
        restTemplateBuilder = new RestTemplateBuilder()
                .additionalMessageConverters(new MappingJackson2HttpMessageConverter(createObjectMapper()));

        censoringRules = Set.of(new RawDataCensoringRule(HttpHeaders.AUTHORIZATION, SENSITIVE_DATA_REPLACEMENT));
    }

    public ExternalRestTemplateBuilderFactory rootUri(final String rootUri) {
        restTemplateBuilder = restTemplateBuilder.rootUri(rootUri);
        return this;
    }

    public ExternalRestTemplateBuilderFactory messageConverters(final HttpMessageConverter<?>... messageConverters) {
        restTemplateBuilder = restTemplateBuilder.messageConverters(messageConverters);
        return this;
    }

    public ExternalRestTemplateBuilderFactory messageConverters(final Collection<? extends HttpMessageConverter<?>> messageConverters) {
        restTemplateBuilder = restTemplateBuilder.messageConverters(messageConverters);
        return this;
    }

    public ExternalRestTemplateBuilderFactory additionalMessageConverters(HttpMessageConverter<?>... messageConverters) {
        restTemplateBuilder = restTemplateBuilder.additionalMessageConverters(messageConverters);
        return this;
    }

    public ExternalRestTemplateBuilderFactory additionalMessageConverters(
            Collection<? extends HttpMessageConverter<?>> messageConverters) {
        restTemplateBuilder = restTemplateBuilder.additionalMessageConverters(messageConverters);
        return this;
    }

    public ExternalRestTemplateBuilderFactory requestFactory(final Supplier<ClientHttpRequestFactory> requestFactorySupplier) {
        restTemplateBuilder = restTemplateBuilder.requestFactory(requestFactorySupplier);
        return this;
    }

    public ExternalRestTemplateBuilderFactory interceptors(final ClientHttpRequestInterceptor... interceptors) {
        restTemplateBuilder = restTemplateBuilder.interceptors(interceptors);
        return this;
    }

    public ExternalRestTemplateBuilderFactory interceptors(final Collection<ClientHttpRequestInterceptor> interceptors) {
        restTemplateBuilder = restTemplateBuilder.interceptors(interceptors);
        return this;
    }

    public ExternalRestTemplateBuilderFactory additionalInterceptors(ClientHttpRequestInterceptor... interceptors) {
        restTemplateBuilder = restTemplateBuilder.additionalInterceptors(interceptors);
        return this;
    }

    public ExternalRestTemplateBuilderFactory additionalInterceptors(Collection<? extends ClientHttpRequestInterceptor> interceptors) {
        restTemplateBuilder = restTemplateBuilder.additionalInterceptors(interceptors);
        return this;
    }

    public ExternalRestTemplateBuilderFactory uriTemplateHandler(final UriTemplateHandler uriTemplateHandler) {
        restTemplateBuilder = restTemplateBuilder.uriTemplateHandler(uriTemplateHandler);
        return this;
    }

    public ExternalRestTemplateBuilderFactory setReadTimeout(Duration readTimeout) {
        restTemplateBuilder = restTemplateBuilder.setReadTimeout(readTimeout);
        return this;
    }

    public ExternalRestTemplateBuilderFactory externalTracing(final String externalTraceIdHeader) {
        restTemplateBuilder = restTemplateBuilder.additionalInterceptors(
                new ExternalTracingClientHttpRequestInterceptor(externalTraceIdHeader));
        return this;
    }

    public ExternalRestTemplateBuilderFactory externalTracing(final String externalTraceIdHeader,
                                                              final Supplier<String> lastExternalTraceIdSupplier) {
        restTemplateBuilder = restTemplateBuilder.additionalInterceptors(
                new ExternalTracingClientHttpRequestInterceptor(externalTraceIdHeader, lastExternalTraceIdSupplier));
        return this;
    }

    public ExternalRestTemplateBuilderFactory rawDataCensoringRules(final Collection<RawDataCensoringRule> censoringRules) {
        this.censoringRules = censoringRules;
        return this;
    }

    public RestTemplate build() {
        LoggingInterceptor loggingInterceptor = new LoggingInterceptor(censoringRules, blacklistedTerms, maxRddBodyToBeMaskedInBytes);
        return restTemplateBuilder
                .errorHandler(new ExternalResponseErrorHandler())
                .additionalInterceptors(new ResponseCodeLoggingInterceptor(), loggingInterceptor)
                .additionalCustomizers(InterceptorSortingRestTemplateCustomizer::customize)
                .build();
    }


    private static ObjectMapper createObjectMapper() {
        Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder = new Jackson2ObjectMapperBuilder();
        jacksonObjectMapperBuilderCustomizer().customize(jacksonObjectMapperBuilder);
        return jacksonObjectMapperBuilder.build();
    }

    private static Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperBuilderCustomizer() {
        return builder -> builder.featuresToDisable(
                // Prevent user data snippets ending up in the logs on JsonParseExceptions
                JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION,
                // Format Dates instead of returning a long
                SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
                // Tolerate new fields
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                // Do not normalize time zone to UTC
                DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
    }

}