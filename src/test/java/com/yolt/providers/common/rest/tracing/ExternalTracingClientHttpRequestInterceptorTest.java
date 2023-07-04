package com.yolt.providers.common.rest.tracing;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ExternalTracingClientHttpRequestInterceptorTest {

    private static final String LAST_EXTERNAL_TRACE_ID_HEADER = "request-trace-id";
    private static final String REQUEST_TRACE_ID_VALUE = "00000000-0000-0000-0000-000000000000";
    private static final String UUID_REGEXP = "^[a-zA-Z0-9]{8}-[a-zA-Z0-9]{4}-4[a-zA-Z0-9]{3}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{12}$";

    private ClientHttpRequestInterceptor interceptor;

    @Test
    public void shouldInterceptWithDefaultLastExternalTraceIdSupplier() throws IOException {
        //given
        interceptor = new ExternalTracingClientHttpRequestInterceptor(LAST_EXTERNAL_TRACE_ID_HEADER);

        HttpHeaders headers = new HttpHeaders();
        headers.set(LAST_EXTERNAL_TRACE_ID_HEADER, REQUEST_TRACE_ID_VALUE);

        // when
        interceptor.intercept(prepareRequest(headers), new byte[0], prepareExecution());

        // then
        String lastExternalTraceId = Objects.requireNonNull(headers.get(LAST_EXTERNAL_TRACE_ID_HEADER)).get(0);

        assertThat(lastExternalTraceId)
                .isNotSameAs(REQUEST_TRACE_ID_VALUE)
                .isSameAs(MDC.get(ExternalTracingUtil.LAST_EXTERNAL_TRACE_ID))
                .matches(UUID_REGEXP);
    }

    @Test
    public void shouldInterceptWithCustomLastExternalTraceIdSupplier() throws IOException {
        // given
        Supplier<String> lastExternalTraceIdSupplier = () -> UUID.randomUUID().toString().replace("-", "");
        interceptor = new ExternalTracingClientHttpRequestInterceptor(LAST_EXTERNAL_TRACE_ID_HEADER, lastExternalTraceIdSupplier);

        HttpHeaders headers = new HttpHeaders();
        headers.set(LAST_EXTERNAL_TRACE_ID_HEADER, REQUEST_TRACE_ID_VALUE);

        // when
        interceptor.intercept(prepareRequest(headers), new byte[0], prepareExecution());

        // then
        String lastExternalTraceId = Objects.requireNonNull(headers.get(LAST_EXTERNAL_TRACE_ID_HEADER)).get(0);

        assertThat(lastExternalTraceId)
                .isNotSameAs(REQUEST_TRACE_ID_VALUE)
                .isSameAs(MDC.get(ExternalTracingUtil.LAST_EXTERNAL_TRACE_ID))
                .doesNotContain("-");
    }

    @Test
    public void shouldInterceptWithVerificationThatLastExternalTraceIdIsChangedDuringNextUsage() throws IOException {
        // given
        interceptor = new ExternalTracingClientHttpRequestInterceptor(LAST_EXTERNAL_TRACE_ID_HEADER);

        HttpHeaders headers = new HttpHeaders();
        headers.set(LAST_EXTERNAL_TRACE_ID_HEADER, REQUEST_TRACE_ID_VALUE);

        // when
        interceptor.intercept(prepareRequest(headers), new byte[0], prepareExecution());
        String lastExternalTraceId1 = Objects.requireNonNull(headers.get(LAST_EXTERNAL_TRACE_ID_HEADER)).get(0);

        interceptor.intercept(prepareRequest(headers), new byte[0], prepareExecution());
        String lastExternalTraceId2 = Objects.requireNonNull(headers.get(LAST_EXTERNAL_TRACE_ID_HEADER)).get(0);

        interceptor.intercept(prepareRequest(headers), new byte[0], prepareExecution());

        // then
        String lastExternalTraceId3 = Objects.requireNonNull(headers.get(LAST_EXTERNAL_TRACE_ID_HEADER)).get(0);

        assertThat(lastExternalTraceId3)
                .isSameAs(MDC.get(ExternalTracingUtil.LAST_EXTERNAL_TRACE_ID));
        assertThat(new HashSet<>(Arrays.asList(lastExternalTraceId1, lastExternalTraceId2, lastExternalTraceId3)))
                .hasSize(3);
    }

    private ClientHttpRequest prepareRequest(HttpHeaders headers) {
        ClientHttpRequest request = Mockito.mock(ClientHttpRequest.class);
        when(request.getHeaders()).thenReturn(headers);
        return request;
    }

    private ClientHttpRequestExecution prepareExecution() throws IOException {
        ClientHttpRequestExecution execution = Mockito.mock(ClientHttpRequestExecution.class);
        when(execution.execute(any(), any())).thenReturn(Mockito.mock(ClientHttpResponse.class));
        return execution;
    }
}
