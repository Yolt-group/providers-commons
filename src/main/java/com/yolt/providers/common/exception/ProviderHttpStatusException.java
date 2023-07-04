package com.yolt.providers.common.exception;

public class ProviderHttpStatusException extends RuntimeException {

    public ProviderHttpStatusException(String message) {
        super(message);
    }

    public ProviderHttpStatusException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
