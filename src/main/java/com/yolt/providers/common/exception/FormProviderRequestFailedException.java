package com.yolt.providers.common.exception;

public class FormProviderRequestFailedException extends RuntimeException {

    public FormProviderRequestFailedException(final Throwable cause) {
        super(cause);
    }

    public FormProviderRequestFailedException(final String message) {
        super(message);
    }

    public FormProviderRequestFailedException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
