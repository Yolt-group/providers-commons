package com.yolt.providers.common.exception;

/**
 * An exception that is thrown when a non 2xx response code is received from a provider.
 * This exception should ONLY be thrown in situations where a simple refresh might solve the issue.
 */
public class ProviderRequestFailedException extends RuntimeException {

    public ProviderRequestFailedException(final String message) {
        super(message);
    }

    public ProviderRequestFailedException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
