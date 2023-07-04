package com.yolt.providers.common.exception;

public abstract class GeneralConfirmException extends Exception {

    GeneralConfirmException(final String msg) {
        super(msg);
    }

    GeneralConfirmException(final Throwable cause) {
        super(cause);
    }

    GeneralConfirmException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
