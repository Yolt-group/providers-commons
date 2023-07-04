package com.yolt.providers.common.rest.http;

import com.yolt.providers.common.exception.BackPressureRequestException;
import com.yolt.providers.common.exception.ProviderHttpStatusException;
import com.yolt.providers.common.exception.TokenInvalidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

@Slf4j
public final class DefaultHttpErrorHandlerV3 implements HttpErrorHandlerV2 {

    static final DefaultHttpErrorHandlerV3 DEFAULT_HTTP_ERROR_HANDLER = new DefaultHttpErrorHandlerV3();

    @Override
    public void handle(final HttpStatusCodeException e, Object additionalParameters) throws TokenInvalidException {
        final String errorMessage;
        final HttpStatus status = e.getStatusCode();
        switch (status) {
            case BAD_REQUEST:
                errorMessage = "Request formed incorrectly: HTTP " + status.value();
                throw new ProviderHttpStatusException(errorMessage, e);
            case UNAUTHORIZED:
                errorMessage = "We are not authorized to call endpoint: HTTP " + status.value();
                throw new TokenInvalidException(errorMessage, e);
            case FORBIDDEN:
                errorMessage = "Access to call is forbidden: HTTP " + status.value();
                throw new TokenInvalidException(errorMessage, e);
            case INTERNAL_SERVER_ERROR:
                errorMessage = "Something went wrong on bank side: HTTP " + status.value();
                throw new ProviderHttpStatusException(errorMessage, e);
            case TOO_MANY_REQUESTS:
                throw new BackPressureRequestException("Too many requests invoked");
            default:
                errorMessage = "Unknown exception: HTTP " + status.value();
                throw new ProviderHttpStatusException(errorMessage, e);
        }
    }
}
