package com.yolt.providers.common.exception;

public class TokenInvalidException extends Exception {

    public TokenInvalidException() {
        this("Invalid token");
    }

    public TokenInvalidException(String message) {
        super(message);
    }

    public TokenInvalidException(String message, Throwable throwable) {
        super(message, throwable);
    }
}