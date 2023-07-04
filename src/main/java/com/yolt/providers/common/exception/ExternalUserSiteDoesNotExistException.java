package com.yolt.providers.common.exception;

public class ExternalUserSiteDoesNotExistException extends HandledProviderCheckedException {

    public ExternalUserSiteDoesNotExistException(Throwable e) {
        super(e);
    }

    public ExternalUserSiteDoesNotExistException(String message) {
        super(message);
    }

    public ExternalUserSiteDoesNotExistException() {

    }
}
