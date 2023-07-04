package com.yolt.providers.common.exception;

import lombok.Getter;

@Getter
public class ProviderFetchDataException extends Exception {
    private static final String DEFAULT_FAILED_ACCOUNTS_MESSAGE = "Failed fetching data";

    public ProviderFetchDataException(Throwable e) {
        super(DEFAULT_FAILED_ACCOUNTS_MESSAGE, e);
    }

    public ProviderFetchDataException() {
        super(DEFAULT_FAILED_ACCOUNTS_MESSAGE);
    }

    public ProviderFetchDataException(String customMessage) {
        super(DEFAULT_FAILED_ACCOUNTS_MESSAGE + ": " + customMessage);
    }
}

