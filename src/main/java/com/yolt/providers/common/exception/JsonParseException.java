package com.yolt.providers.common.exception;

import lombok.Getter;

public class JsonParseException extends RuntimeException {

    @Getter
    private final String jsonResponse;

    public JsonParseException(String msg, String json) {
        super(msg);
        this.jsonResponse = json;
    }

    public JsonParseException(Throwable cause, String json) {
        super(cause);
        this.jsonResponse = json;
    }
}
