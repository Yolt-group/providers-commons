package com.yolt.providers.common.exception;

public class GetLoginInfoUrlFailedException extends RuntimeException {

    public GetLoginInfoUrlFailedException(final String message) {
        super(message);
    }

    public GetLoginInfoUrlFailedException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
