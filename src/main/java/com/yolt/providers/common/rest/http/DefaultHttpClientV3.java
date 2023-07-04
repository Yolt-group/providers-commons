package com.yolt.providers.common.rest.http;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.yolt.providers.common.exception.TokenInvalidException;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
public class DefaultHttpClientV3 implements HttpClient {

    private static final String TIMER_METRIC_NAME = "restclient.url.providers.request.duration";
    private static final String HTTP_STATUS_TAG = "http_status";
    private static final String SERVICE_NAME_TAG = "service_name";
    private static final String SERVICE_PATH_TAG = "service_path";

    private final MeterRegistry registry;
    private final RestTemplate restTemplate;
    private final String provider;

    @Override
    public <T> ResponseEntity<T> exchange(final String endpoint,
                                          final HttpMethod method,
                                          final HttpEntity body,
                                          final String prometheusPathOverride,
                                          final Class<T> responseType,
                                          final Object errorHandlerParameters,
                                          final HttpErrorHandlerV2 errorHandler,
                                          final String... uriArgs) throws TokenInvalidException {
        Timer.Sample sample = Timer.start(registry);
        try {
            ResponseEntity<T> response = restTemplate.exchange(endpoint, method, body, responseType, uriArgs);
            Timer timer = getTimer(provider, prometheusPathOverride, response.getStatusCode().toString());
            sample.stop(timer);
            return response;
        } catch (HttpStatusCodeException e) {
            Timer timer = getTimer(provider, prometheusPathOverride, e.getStatusCode().toString());
            sample.stop(timer);
            errorHandler.handle(e, errorHandlerParameters);
            log.info("Request failed on endpoint = {}", endpoint); //NOSHERIFF Checking on what endpoint request failed
            return null;
        } catch (RestClientException e) {
            Throwable cause = e.getRootCause();
            if (cause instanceof MismatchedInputException) {
                Timer timer = getTimer(provider, prometheusPathOverride, "JSON_PARSE_ERROR");
                sample.stop(timer);
                throw e;
            } else {
                Timer timer = getTimer(provider, prometheusPathOverride, "-1");
                sample.stop(timer);
                throw e;
            }
        }
    }

    private Timer getTimer(final String provider, final String prometheusPathOverride, final String statusCodeString) {
        return registry.timer(TIMER_METRIC_NAME,
                SERVICE_NAME_TAG, provider,
                SERVICE_PATH_TAG, prometheusPathOverride,
                HTTP_STATUS_TAG, statusCodeString);
    }
}
