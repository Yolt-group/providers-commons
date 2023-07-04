package com.yolt.providers.common.rest;

import com.yolt.providers.common.domain.logging.RawDataCensoringRule;
import com.yolt.providers.common.rest.logging.LoggingInterceptor;
import com.yolt.providers.common.rest.monitoring.ResponseCodeLoggingInterceptor;
import org.junit.jupiter.api.Test;
import org.springframework.core.Ordered;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class ExternalRestTemplateBuilderFactoryTest {

    @Test
    public void shouldExternalTracingPassVerificationOfUniqueness() {
        // given
        String duplicatedTracingValue = "request-trace-id";

        // when
        RestTemplate restTemplate = new ExternalRestTemplateBuilderFactory()
                .externalTracing(duplicatedTracingValue)
                .externalTracing(duplicatedTracingValue)
                .build();

        // then
        // default ResponseCodeLoggingInterceptor + ExternalTracingClientHttpRequestInterceptor + LoggingInterceptor
        assertThat(restTemplate.getInterceptors()).hasSize(3);
    }

    @Test
    public void shouldRestTemplateErrorHandlerBeInstanceOfExternalResponseErrorHandlerClass() {
        // when
        RestTemplate restTemplate = new ExternalRestTemplateBuilderFactory()
                .build();

        // then
        assertThat(restTemplate.getErrorHandler()).isInstanceOf(ExternalResponseErrorHandler.class);
    }

    @Test
    public void shouldReturnRestTemplateWithTwoInterceptorsWithDefaultSettingsSortedByOrder() {
        // when
        RestTemplate restTemplate = new ExternalRestTemplateBuilderFactory()
                .build();

        // then
        assertThat(restTemplate.getInterceptors()).hasSize(2);
        assertThat(restTemplate.getInterceptors()).element(0).isInstanceOf(ResponseCodeLoggingInterceptor.class);
        assertThat(restTemplate.getInterceptors()).element(1).isInstanceOf(LoggingInterceptor.class);

        assertThat(((Ordered) restTemplate.getInterceptors().get(0)).getOrder()
                < ((Ordered) restTemplate.getInterceptors().get(1)).getOrder()).isTrue();

        assertThat(((LoggingInterceptor) restTemplate.getInterceptors().get(1)).getCensoredHeadersProducer()).isNotNull();
    }

    @Test
    public void shouldReturnRestTemplateWithCustomizedLoggingInterceptor() {
        // given
        List<RawDataCensoringRule> rawDataCensoringRules = new ArrayList<>();
        rawDataCensoringRules.add(new RawDataCensoringRule("Authorization", "Bearer ***"));

        // when
        RestTemplate restTemplate = new ExternalRestTemplateBuilderFactory()
                .rawDataCensoringRules(rawDataCensoringRules)
                .build();

        // then
        assertThat(restTemplate.getInterceptors()).hasSize(2);
        assertThat(restTemplate.getInterceptors().get(0)).isInstanceOf(ResponseCodeLoggingInterceptor.class);
        assertThat(restTemplate.getInterceptors().get(1)).isInstanceOf(LoggingInterceptor.class);

        assertThat(((LoggingInterceptor) restTemplate.getInterceptors().get(1)).getCensoredHeadersProducer()).isNotNull();
    }

    @Test
    public void shouldReturnRestTemplateWithCustomizedLoggingInterceptorWithoutDuplicates() {
        // given
        List<RawDataCensoringRule> rawDataCensoringRules = new ArrayList<>();
        rawDataCensoringRules.add(new RawDataCensoringRule("Authorization", "Bearer ***"));

        List<RawDataCensoringRule> newerRawDataCensoringRules = new ArrayList<>();
        rawDataCensoringRules.add(new RawDataCensoringRule("Authorization", "Bearer ***** ***"));

        // when
        RestTemplate restTemplate = new ExternalRestTemplateBuilderFactory()
                .rawDataCensoringRules(rawDataCensoringRules)
                .rawDataCensoringRules(newerRawDataCensoringRules)
                .build();

        // then
        assertThat(restTemplate.getInterceptors()).hasSize(2);
        assertThat(restTemplate.getInterceptors().get(0)).isInstanceOf(ResponseCodeLoggingInterceptor.class);
        assertThat(restTemplate.getInterceptors().get(1)).isInstanceOf(LoggingInterceptor.class);

        assertThat(((LoggingInterceptor) restTemplate.getInterceptors().get(1)).getCensoredHeadersProducer()).isNotNull();
    }






}
