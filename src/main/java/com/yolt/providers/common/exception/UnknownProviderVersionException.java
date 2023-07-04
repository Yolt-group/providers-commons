package com.yolt.providers.common.exception;

/**
 * Thrown when there is no such version known for a given provider.
 */
public class UnknownProviderVersionException extends RuntimeException {

    public UnknownProviderVersionException(final String message) {
        super(message);
    }
}