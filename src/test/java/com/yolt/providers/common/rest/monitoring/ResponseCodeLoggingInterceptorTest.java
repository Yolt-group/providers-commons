package com.yolt.providers.common.rest.monitoring;

import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ResponseCodeLoggingInterceptorTest {

    private ClientHttpRequestInterceptor subject = new ResponseCodeLoggingInterceptor();

    @Test
    void shouldInterceptAndSetStatusCodeInMDCContext() throws IOException {
        //given
        MDC.remove(ResponseCodeLoggingInterceptor.LAST_EXTERNAL_STATUS_CODE);
        int expectedStatusCode = 200;

        // when
        subject.intercept(mock(ClientHttpRequest.class), new byte[0], prepareExecution(expectedStatusCode));

        // then
        assertThat(MDC.get(ResponseCodeLoggingInterceptor.LAST_EXTERNAL_STATUS_CODE)).isEqualTo(String.valueOf(expectedStatusCode));
    }

    private ClientHttpRequestExecution prepareExecution(int mockStatusCode) throws IOException {
        ClientHttpRequestExecution execution = mock(ClientHttpRequestExecution.class);
        ClientHttpResponse mockResponse = mock(ClientHttpResponse.class);

        when(execution.execute(any(), any())).thenReturn(mockResponse);
        when(mockResponse.getRawStatusCode()).thenReturn(mockStatusCode);

        return execution;
    }
}