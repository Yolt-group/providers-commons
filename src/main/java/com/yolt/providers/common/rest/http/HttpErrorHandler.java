package com.yolt.providers.common.rest.http;

import com.yolt.providers.common.exception.TokenInvalidException;
import org.springframework.web.client.HttpStatusCodeException;

/**
 * @deprecated Use HttpErrorHandlerV2, remove in C4PO-7297
 */
@Deprecated
@FunctionalInterface
public interface HttpErrorHandler {

    void handle(HttpStatusCodeException e) throws TokenInvalidException;
}
