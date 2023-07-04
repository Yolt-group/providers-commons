package com.yolt.providers.common.exception;

public class AuthenticationMeanValidationException extends RuntimeException {

    public AuthenticationMeanValidationException(final String message) {
        super(message);
    }

    public AuthenticationMeanValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}