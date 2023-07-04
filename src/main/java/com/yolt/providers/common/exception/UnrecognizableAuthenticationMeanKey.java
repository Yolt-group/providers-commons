package com.yolt.providers.common.exception;

public class UnrecognizableAuthenticationMeanKey extends RuntimeException {

    public UnrecognizableAuthenticationMeanKey(final String message) {
        super(message);
    }
}