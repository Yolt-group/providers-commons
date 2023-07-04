package com.yolt.providers.common.rest.http;

import com.yolt.providers.common.exception.ProviderHttpStatusException;
import com.yolt.providers.common.exception.TokenInvalidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
/**
 * @deprecated use DefaultHttpErrorHandlerV3
 */
@Deprecated
@Slf4j
public final class DefaultHttpErrorHandlerV2 implements HttpErrorHandlerV2 {

    static final DefaultHttpErrorHandlerV2 DEFAULT_HTTP_ERROR_HANDLER = new DefaultHttpErrorHandlerV2();

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
            default:
                errorMessage = "Unknown exception: HTTP " + status.value();
                throw new ProviderHttpStatusException(errorMessage, e);
        }
    }
}
