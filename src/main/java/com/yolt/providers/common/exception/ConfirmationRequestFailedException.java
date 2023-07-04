package com.yolt.providers.common.exception;

public class ConfirmationRequestFailedException extends GeneralConfirmException {

    public ConfirmationRequestFailedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ConfirmationRequestFailedException(final String message) {
        super(message);
    }
}
