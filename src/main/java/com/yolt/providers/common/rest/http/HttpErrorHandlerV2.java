package com.yolt.providers.common.rest.http;

import com.yolt.providers.common.exception.TokenInvalidException;
import org.springframework.web.client.HttpStatusCodeException;

@FunctionalInterface
public interface HttpErrorHandlerV2 {

    void handle(HttpStatusCodeException e, Object param) throws TokenInvalidException;
}
