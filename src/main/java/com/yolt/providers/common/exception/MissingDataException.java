package com.yolt.providers.common.exception;

/**
 * Exception is thrown by {@link com.yolt.providers.common.providerinterface.UrlDataProvider} when data
 * received from bank are incomplete.
 */
public class MissingDataException extends RuntimeException {

    public MissingDataException(final String message) {
        super(message);
    }

}
