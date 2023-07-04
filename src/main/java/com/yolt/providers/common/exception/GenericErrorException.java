package com.yolt.providers.common.exception;

public class GenericErrorException extends HandledProviderCheckedException {

    public GenericErrorException() {
    }

    public GenericErrorException(String message) {
        super(message);
    }

}
