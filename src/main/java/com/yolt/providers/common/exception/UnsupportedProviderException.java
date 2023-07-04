package com.yolt.providers.common.exception;

/**
 * Thrown when there is no implementation for given provider.
 */
public class UnsupportedProviderException extends RuntimeException {

    public UnsupportedProviderException(final String message) {
        super(message);
    }
}