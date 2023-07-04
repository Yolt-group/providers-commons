package com.yolt.providers.common.exception;

/**
 * Thrown when a payment creation results in a failure which is not the user's fault.
 */
public class CreationFailedException extends GeneralCreateException {

    public CreationFailedException(final Throwable e) {
        super(e);
    }

    public CreationFailedException(final String message) {
        super(message);
    }

    public CreationFailedException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
