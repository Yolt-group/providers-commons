package com.yolt.providers.common.rest.http;

import com.yolt.providers.common.exception.TokenInvalidException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static com.yolt.providers.common.rest.http.DefaultHttpErrorHandler.DEFAULT_HTTP_ERROR_HANDLER;

public interface HttpClient {

    /**
     * @deprecated Use method with error handler parameters, remove in C4PO-7297
     */
    @Deprecated
    default <T> ResponseEntity<T> exchange(final String endpoint,
                                   final HttpMethod method,
                                   final HttpEntity body,
                                   final String prometheusPathOverride,
                                   final Class<T> responseType,
                                   final HttpErrorHandler errorHandler,
                                   final String... uriArgs) throws TokenInvalidException {
        throw new UnsupportedOperationException("Existing implementations have this method implemented, new implementations should not use it.");
    }

    default <T> ResponseEntity<T> exchange(final String endpoint,
                                           final HttpMethod method,
                                           final HttpEntity body,
                                           final String prometheusPathOverride,
                                           final Class<T> responseType,
                                           final String... uriArgs) throws TokenInvalidException {
        return exchange(endpoint, method, body, prometheusPathOverride, responseType,
                DEFAULT_HTTP_ERROR_HANDLER, uriArgs);
    }

    default <T> T exchangeForBody(final String endpoint,
                                  final HttpMethod method,
                                  final HttpEntity body,
                                  final String prometheusPathOverride,
                                  final Class<T> responseType,
                                  final String... uriArgs) throws TokenInvalidException {
        return exchange(endpoint, method, body, prometheusPathOverride, responseType,
                DEFAULT_HTTP_ERROR_HANDLER, uriArgs).getBody();
    }

    <T> ResponseEntity<T> exchange(String endpoint,
                                   HttpMethod method,
                                   HttpEntity body,
                                   String prometheusPathOverride,
                                   Class<T> responseType,
                                   Object errorHandlerParameters,
                                   HttpErrorHandlerV2 errorHandler,
                                   String... uriArgs) throws TokenInvalidException;

    default <T> ResponseEntity<T> exchange(final String endpoint,
                                           final HttpMethod method,
                                           final HttpEntity body,
                                           final String prometheusPathOverride,
                                           final Class<T> responseType,
                                           final HttpErrorHandlerV2 errorHandler,
                                           final String... uriArgs) throws TokenInvalidException {
        return exchange(endpoint, method, body, prometheusPathOverride, responseType, null, errorHandler, uriArgs);
    }
}