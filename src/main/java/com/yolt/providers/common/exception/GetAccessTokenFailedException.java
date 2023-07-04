package com.yolt.providers.common.exception;

public class GetAccessTokenFailedException extends RuntimeException {

    public GetAccessTokenFailedException(final Throwable t) {
        super(t);
    }

    public GetAccessTokenFailedException(final String message) {
        super(message);
    }

    public GetAccessTokenFailedException(final String message, final Throwable t) {
        super(message, t);
    }
}
