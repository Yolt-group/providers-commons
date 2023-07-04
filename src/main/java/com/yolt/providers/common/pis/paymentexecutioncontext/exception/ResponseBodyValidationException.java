package com.yolt.providers.common.pis.paymentexecutioncontext.exception;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

@Getter
public class ResponseBodyValidationException extends Exception {

    private final JsonNode rawResponseBody;

    public ResponseBodyValidationException(JsonNode rawResponseBody, String message) {
        super(message);
        this.rawResponseBody = rawResponseBody;
    }
}
