package com.yolt.providers.common.rest.intercepting;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * This class serves a purpose of wrapping ClientHttpRequestInterceptor instances that
 * doesn't implement Ordered interface to allow sorting interceptors by Order value
 */
@Getter
@RequiredArgsConstructor
public class DefaultOrderClientHttpRequestInterceptorWrapper implements ClientHttpRequestInterceptor, Ordered {

    /**
     * For explanation please consult README section dedicated to order of interceptors execution
     */
    public static final int ORDER = 0;

    @NonNull
    private final ClientHttpRequestInterceptor delegate;

    @Override
    public int getOrder() {
        return ORDER;
    }

    @Override
    public ClientHttpResponse intercept(final HttpRequest request, final byte[] body, final ClientHttpRequestExecution execution) throws IOException {
        return delegate.intercept(request, body, execution);
    }
}
