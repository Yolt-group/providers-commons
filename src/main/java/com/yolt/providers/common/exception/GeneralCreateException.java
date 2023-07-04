package com.yolt.providers.common.exception;

public abstract class GeneralCreateException extends Exception {

    GeneralCreateException(final String message) {
        super(message);
    }

    GeneralCreateException(final String message, final Throwable cause) {
        super(message, cause);
    }

    GeneralCreateException(final Throwable cause) {
        super(cause);
    }
}
