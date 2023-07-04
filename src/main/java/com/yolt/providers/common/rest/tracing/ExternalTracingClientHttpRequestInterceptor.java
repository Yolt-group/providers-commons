package com.yolt.providers.common.rest.tracing;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * This interceptor is used to not hand on internal request trace id for outgoing calls and to handle creation of unique external trace id for each outgoing call.
 *
 * In general it sets a new value for the header, which is specified by lastExternalTraceIdHeader and during this,
 * value is also stored in MDC under last_external_trace_id key.
 * The last_external_trace_id key is not cleared after intercepting a request, but it is overwritten on the next call.
 *
 * A new value will be created based on supplier definition but also if lastExternalTraceIdSupplier is not specified,
 * then it creates random UUID string by default.
 *
 * Additionally, the supplier field is excluded from hashcode calculation to hold it's uniqueness based on lastExternalTraceIdHeader value.
 */
@Slf4j
@AllArgsConstructor
@EqualsAndHashCode(exclude = "lastExternalTraceIdSupplier")
public class ExternalTracingClientHttpRequestInterceptor implements ClientHttpRequestInterceptor, Ordered {

    /**
     * For explanation please consult README section dedicated to order of interceptors execution
     */
    public static final int ORDER = -100;

    private final String lastExternalTraceIdHeader;
    private final Supplier<String> lastExternalTraceIdSupplier;

    public ExternalTracingClientHttpRequestInterceptor(String lastExternalTraceIdHeader) {
        this.lastExternalTraceIdHeader = lastExternalTraceIdHeader;
        this.lastExternalTraceIdSupplier = () -> UUID.randomUUID().toString();
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().set(lastExternalTraceIdHeader, ExternalTracingUtil.createLastExternalTraceId(lastExternalTraceIdSupplier));
        return execution.execute(request, body);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}

