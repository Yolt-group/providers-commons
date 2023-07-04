package com.yolt.providers.common.rest.http;

import com.yolt.providers.common.exception.BackPressureRequestException;
import com.yolt.providers.common.exception.TokenInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class TooManyRequestsErrorHandler implements HttpErrorHandlerV2 {

    @Override
    public void handle(HttpStatusCodeException e, Object param) throws TokenInvalidException {
        if (HttpStatus.TOO_MANY_REQUESTS.equals(e.getStatusCode())) {
            throw new BackPressureRequestException("Too many requests invoked");
        }
        throw e;
    }
}
