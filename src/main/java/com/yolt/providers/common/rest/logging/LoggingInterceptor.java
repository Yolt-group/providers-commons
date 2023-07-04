package com.yolt.providers.common.rest.logging;

import com.yolt.providers.common.domain.logging.RawDataCensoringRule;
import com.yolt.providers.common.rest.http.WrappedClientHttpResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.slf4j.Marker;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static net.logstash.logback.marker.Markers.append;

/**
 * <h1>Http traffic logger</h1>
 *
 * <h2>Log markers: requests & responses</h2>
 * <dl>
 * <dt>http-call-id</dt> <dd>a correlation id added to every messages related to the same request/response pair</dd>
 * <dt>http-headers</dt> <dd> a string containing the http headers present on the request/response</dd>
 * </dl>
 *
 * <h2>Log markers: requests</h2>
 * <dl>
 * <dt>http-is-request</dt> <dd>always 'true'</dd>
 * <dt>http-method</dt> <dd> the request method</dd>
 * <dt>http-host</dt> <dd> the remote host</dd>
 * <dt>http-uri</dt> <dd> the entire request uri</dd>
 * </dl>
 *
 * <h2>Log markers: responses</h2>
 * <dl>
 * <dt>http-is-response</dt> <dd>always 'true'</dd>
 * <dt>http-status-code</dt> <dd> the http response code</dd>
 * <dt>http-call-duration-ms</dt> <dd> the request duration in milliseconds</dd>
 * </dl>
 */
@Slf4j
@Getter
@EqualsAndHashCode
public class LoggingInterceptor implements ClientHttpRequestInterceptor, Ordered {

    /**
     * For explanation please consult README section dedicated to order of interceptors execution
     */
    public static final int ORDER = 200;
    public static final String REQUEST_RESPONSE_DTO_BINDING_CALL_ID = "REQUEST_RESPONSE_DTO_BINDING_CALL_ID";

    private static final String JSON_KEY_VALUE_SEPARATOR = "\":\"";
    private static final String FORM_AND_URL_KEY_VALUE_SEPARATOR = "=";
    private static final String JSON_REGEX_PART = "[^\"]*";
    private static final String FORM_AND_URL_REGEX_PART = "[^&]*";
    private static final String SENSITIVE_DATA_REPLACEMENT = "********";

    private final CensoredHeadersProducer censoredHeadersProducer;
    private final Map<String, KeywordReplacementData> jsonKeywordWithReplacement;
    private final Map<String, KeywordReplacementData> formAndUrlKeywordWithReplacement;
    private final int maxRddBodyToBeMaskedInBytes;

    public LoggingInterceptor(final Collection<RawDataCensoringRule> rawDataCensoringRules, Set<String> blacklistedTerms, int maxRddBodyToBeMaskedInBytes) {
        this.censoredHeadersProducer = new CensoredHeadersProducer(rawDataCensoringRules);
        this.jsonKeywordWithReplacement = generateKeywordMap(blacklistedTerms, JSON_KEY_VALUE_SEPARATOR, JSON_REGEX_PART);
        this.formAndUrlKeywordWithReplacement = generateKeywordMap(blacklistedTerms, FORM_AND_URL_KEY_VALUE_SEPARATOR, FORM_AND_URL_REGEX_PART);
        this.maxRddBodyToBeMaskedInBytes = maxRddBodyToBeMaskedInBytes;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        // An id with which a user can correlate request and response messages in the log.
        final UUID requestResponseCorrelationId = UUID.randomUUID();

        HttpHeaders headers = request.getHeaders();

        Marker requestMarker = append("http-call-id", requestResponseCorrelationId)
                .and(append("http-is-request", true))
                .and(append("http-method", request.getMethodValue()))
                .and(append("http-host", request.getURI().getHost()))
                .and(append("http-uri", getCensoredUrl(request.getURI().toString())))
                .and(append("http-headers", getCensoredHeaders(headers).toString()))
                .and(append("raw-data", "true"))
                .and(append("raw-data-type", RawDataType.RDD_REQUEST))
                .and(append("raw-data-call-id", MDC.get(REQUEST_RESPONSE_DTO_BINDING_CALL_ID))); //NOSHERIFF as indicated in C4PO-9255 this should be placed here


        // Log request.
        String requestBody = null;
        if (body != null) {
            requestBody = prepareBodyToLog(body);
        }
        log.debug(requestMarker, StringUtils.isEmpty(requestBody) ? "<no request body>" : requestBody);

        long requestStartTimeNs = System.nanoTime();
        WrappedClientHttpResponse response = new WrappedClientHttpResponse(execution.execute(request, body));
        long requestEndTimeNs = System.nanoTime();
        logErrorResponseComingFromProxy(response);

        headers = response.getHeaders();

        Marker responseMarker = append("http-call-id", requestResponseCorrelationId)
                .and(append("http-is-response", true))
                .and(append("http-status-code", response.getRawStatusCode()))
                .and(append("http-headers", getCensoredHeaders(headers).toString()))
                .and(append("http-call-duration-ms", NANOSECONDS.toMillis(requestEndTimeNs - requestStartTimeNs)))
                .and(append("raw-data", "true"))
                .and(append("raw-data-type", RawDataType.RDD_RESPONSE))
                .and(append("raw-data-call-id", MDC.get(REQUEST_RESPONSE_DTO_BINDING_CALL_ID))); //NOSHERIFF as indicated in C4PO-9255 this should be placed here


        // Log response.
        final String responseBody = prepareBodyToLog(response.getBytes());
        log.debug(responseMarker, StringUtils.isEmpty(responseBody) ? "<no response body>" : responseBody);

        return response;
    }

    private String prepareBodyToLog(byte[] bodyBytes) {
        String body = new String(bodyBytes);
        if (bodyBytes.length > maxRddBodyToBeMaskedInBytes) {
            return body;
        }

        if (isJson(body)) {
            body = attemptMaskingBody(body, jsonKeywordWithReplacement);
        } else {
            body = attemptMaskingBody(body, formAndUrlKeywordWithReplacement);
        }
        return body;
    }

    private String attemptMaskingBody(String body, Map<String, KeywordReplacementData> keywordWithReplacement) {
        for (Map.Entry<String, KeywordReplacementData> entry : keywordWithReplacement.entrySet()) {
            if (body.contains(entry.getKey())) {
                KeywordReplacementData replacementData = entry.getValue();
                body = replacementData.getRegex().matcher(body).replaceAll(replacementData.getReplacement());
            }
        }
        return body;
    }

    private boolean isJson(String body) {
        return !StringUtils.isEmpty(body) && body.charAt(0) == '{';
    }

    /**
     * Logs the error if it encounters proxy specific issues.
     */
    private void logErrorResponseComingFromProxy(ClientHttpResponse httpResponse) {
        HttpHeaders responseHeaders = httpResponse.getHeaders();
        if (responseHeaders.containsKey("X-Squid-Error")) {
            log.warn("The http(s) proxy has returned an error: {}", responseHeaders.getFirst("X-Squid-Error")); //NOSHERIFF as indicated in C4PO-2796 this should be logged here
        }
    }

    private HttpHeaders getCensoredHeaders(HttpHeaders headers) {
        if (censoredHeadersProducer != null) {
            return censoredHeadersProducer.getCensoredHeaders(headers);
        }
        return headers;
    }

    public String getCensoredUrl(String url) {
        for (Map.Entry<String, LoggingInterceptor.KeywordReplacementData> entry : formAndUrlKeywordWithReplacement.entrySet()) {
            if (url.contains(entry.getKey())) {
                url = entry.getValue().getRegex().matcher(url).replaceAll(entry.getValue().getReplacement());
            }
        }
        return url;
    }

    private Map<String, KeywordReplacementData> generateKeywordMap(Set<String> blacklistedTerms, String separator, String regexPart) {
        if (CollectionUtils.isEmpty(blacklistedTerms)) {
            return Collections.emptyMap();
        }
        return blacklistedTerms.stream().collect(Collectors.toMap(
                term -> term + separator,
                term -> new KeywordReplacementData(Pattern.compile(term + separator + regexPart), term + separator + SENSITIVE_DATA_REPLACEMENT)
        ));
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

    @Data
    @AllArgsConstructor
    static class KeywordReplacementData {
        private Pattern regex;
        private String replacement;
    }
}
