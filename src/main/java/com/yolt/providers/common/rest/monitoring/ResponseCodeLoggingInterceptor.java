package com.yolt.providers.common.rest.monitoring;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * This interceptor is used to put in MDC context (for better monitoring) status code received from last external HTTP response.
 */
@Slf4j
@AllArgsConstructor
@EqualsAndHashCode
public class ResponseCodeLoggingInterceptor implements ClientHttpRequestInterceptor, Ordered {

    static final String LAST_EXTERNAL_STATUS_CODE = "last_external_status_code";

    /**
     * For explanation please consult README section dedicated to order of interceptors execution
     */
    public static final int ORDER = -200;

    @Override
    public ClientHttpResponse intercept(HttpRequest request,
                                        byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        ClientHttpResponse execute = execution.execute(request, body);
        MDC.put(LAST_EXTERNAL_STATUS_CODE, String.valueOf(execute.getRawStatusCode()));
        return execute;
    }


    @Override
    public int getOrder() {
        return ORDER;
    }
}

