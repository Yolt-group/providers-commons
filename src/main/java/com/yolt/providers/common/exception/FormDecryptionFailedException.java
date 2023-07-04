package com.yolt.providers.common.exception;

public class FormDecryptionFailedException extends RuntimeException {

    public FormDecryptionFailedException(String message) {
        super(message);
    }

    public FormDecryptionFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}