package com.yolt.providers.common.exception;

public class CallbackJsonParseException extends Exception {
    public CallbackJsonParseException(String message) {
        super(message);
    }

    public CallbackJsonParseException(Exception e) {
        super(e);
    }

    public CallbackJsonParseException(String message, Exception e) {
        super(message, e);
    }
}
