package com.yolt.providers.common.rest.intercepting;

import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

@RequiredArgsConstructor
public class MockOrderedClientHttpRequestInterceptor implements ClientHttpRequestInterceptor, Ordered {

    private final int order;

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public ClientHttpResponse intercept(final HttpRequest request, final byte[] body, final ClientHttpRequestExecution execution) throws IOException {
        return null;
    }
}
