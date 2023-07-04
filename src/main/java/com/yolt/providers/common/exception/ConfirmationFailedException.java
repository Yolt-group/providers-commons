package com.yolt.providers.common.exception;

public class ConfirmationFailedException extends GeneralConfirmException {

    public ConfirmationFailedException(final Throwable cause) {
        super(cause);
    }

    public ConfirmationFailedException(final String message) {
        super(message);
    }

    public ConfirmationFailedException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
