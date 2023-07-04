package com.yolt.providers.common.rest.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.yolt.providers.common.domain.logging.RawDataCensoringRule;
import com.yolt.providers.common.rest.http.WrappedClientHttpResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Moved from {@link ConditionalHttpTrafficLoggingRequestFactoryTest} which is removed now (added for Git history sniffers)
 */
@ExtendWith(MockitoExtension.class)
public class LoggingInterceptorTest {

    private static final int MAXIMUM_MASKED_RDD_SIZE_IN_BYTES = 3000;
    private static final Set<String> BLACKLISTED_TERMS = Set.of("access_token", "accessToken", "refresh_token", "refreshToken",
            "username", "password", "clientSecret", "client_secret");
    private static final String SENSITIVE_DATA_REPLACEMENT = "********";

    private static final String LARGE_BODY_TEMPLATE = """
            {"accessToken":"access_token_that_should_be_visible",
            "big_chunk_of_data":"%s"}
            """;
    private static final String LARGE_BODY = String.format(LARGE_BODY_TEMPLATE, "a".repeat(3000));
    private LoggingInterceptor subject = new LoggingInterceptor(
            Set.of(new RawDataCensoringRule(HttpHeaders.AUTHORIZATION, SENSITIVE_DATA_REPLACEMENT)),
            BLACKLISTED_TERMS,
            MAXIMUM_MASKED_RDD_SIZE_IN_BYTES);

    protected Appender<ILoggingEvent> mockAppender;
    protected ArgumentCaptor<ILoggingEvent> captorLoggingEvent;

    @BeforeEach
    public void beforeEach() {
        mockAppender = mock(Appender.class);
        captorLoggingEvent = ArgumentCaptor.forClass(ILoggingEvent.class);
        final Logger logger = (Logger) LoggerFactory.getLogger(LoggingInterceptor.class);
        logger.setLevel(Level.ALL);
        logger.addAppender(mockAppender);
    }

    @AfterEach
    public void afterEach() {
        final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.detachAppender(mockAppender);
    }

    /**
     * Request with marker with `null` request body / `null` response body should be logged.
     */
    @Test
    @SneakyThrows
    public void shouldLogCallWhenInterceptingRequestWithoutBody() {
        Mocks mocks = new Mocks();
        when(mocks.execution.execute(any(), any())).thenReturn(mocks.response);
        when(mocks.request.getHeaders()).thenReturn(HttpHeaders.EMPTY);
        when(mocks.request.getURI()).thenReturn(URI.create("https://www.example.com"));
        when(mocks.response.getHeaders()).thenReturn(HttpHeaders.EMPTY);

        // when
        ClientHttpResponse response = subject.intercept(mocks.request, null, mocks.execution);

        // then
        assertThat(response).isInstanceOf(WrappedClientHttpResponse.class);
        verify(mockAppender, times(2)).doAppend(captorLoggingEvent.capture());
        List<ILoggingEvent> values = captorLoggingEvent.getAllValues();
        ILoggingEvent requestLogline = values.get(0);
        ILoggingEvent responseLogline = values.get(1);
        assertThat(requestLogline.getMessage()).isEqualTo("<no request body>");
        assertThat(responseLogline.getMessage()).isEqualTo("<no response body>");
        verifyMarkers(requestLogline, responseLogline);
    }

    /**
     * Request with marker with `new byte[0]` body should be logged.
     */
    @Test
    @SneakyThrows
    public void shouldLogCallWhenInterceptingRequestWithZeroLengthBody() {
        // given
        Mocks mocks = new Mocks();
        when(mocks.execution.execute(any(), any())).thenReturn(mocks.response);
        when(mocks.request.getHeaders()).thenReturn(HttpHeaders.EMPTY);
        when(mocks.request.getURI()).thenReturn(URI.create("https://www.example.com"));
        when(mocks.response.getHeaders()).thenReturn(HttpHeaders.EMPTY);

        // when
        ClientHttpResponse response = subject.intercept(mocks.request, "".getBytes(), mocks.execution);

        // then
        assertThat(response).isInstanceOf(WrappedClientHttpResponse.class);
        verify(mockAppender, times(2)).doAppend(captorLoggingEvent.capture());
        List<ILoggingEvent> values = captorLoggingEvent.getAllValues();
        ILoggingEvent requestLogline = values.get(0);
        ILoggingEvent responseLogline = values.get(1);
        assertThat(requestLogline.getMessage()).isEqualTo("<no request body>");
        assertThat(responseLogline.getMessage()).isEqualTo("<no response body>");
        verifyNoMoreInteractions(mockAppender);
    }

    /**
     * Request with marker with `new byte[n], n > 0` body should be logged.
     */
    @Test
    @SneakyThrows
    public void shouldLogCallWhenInterceptingRequestWithBody() {
        // given
        Mocks mocks = new Mocks();
        when(mocks.execution.execute(any(), any())).thenReturn(mocks.response);
        when(mocks.request.getHeaders()).thenReturn(HttpHeaders.EMPTY);
        when(mocks.request.getURI()).thenReturn(URI.create("https://www.example.com"));
        when(mocks.response.getHeaders()).thenReturn(HttpHeaders.EMPTY);

        // when
        ClientHttpResponse response = subject.intercept(mocks.request, "{\"json\":\"yup\"}".getBytes(), mocks.execution);

        // then
        assertThat(response).isInstanceOf(WrappedClientHttpResponse.class);
        verify(mockAppender, times(2)).doAppend(captorLoggingEvent.capture());
        List<ILoggingEvent> values = captorLoggingEvent.getAllValues();
        ILoggingEvent requestLogline = values.get(0);
        ILoggingEvent responseLogline = values.get(1);
        assertThat(requestLogline.getMessage()).isEqualTo("{\"json\":\"yup\"}");
        assertThat(responseLogline.getMessage()).isEqualTo("<no response body>");
        verifyNoMoreInteractions(mockAppender);
    }

    /**
     * Response with marker with `new byte[0]` body should be logged.
     */
    @Test
    @SneakyThrows
    public void shouldLogCallWhenInterceptingResponseWithZeroLengthBody() {
        // given
        Mocks mocks = new Mocks();
        when(mocks.execution.execute(any(), any())).thenReturn(mocks.response);
        when(mocks.request.getHeaders()).thenReturn(HttpHeaders.EMPTY);
        when(mocks.request.getURI()).thenReturn(URI.create("https://www.example.com"));
        when(mocks.response.getHeaders()).thenReturn(HttpHeaders.EMPTY);
        when(mocks.response.getBody()).thenReturn(new ByteArrayInputStream(new byte[0]));

        // when
        ClientHttpResponse response = subject.intercept(mocks.request, null, mocks.execution);

        // then
        assertThat(response).isInstanceOf(WrappedClientHttpResponse.class);
        verify(mockAppender, times(2)).doAppend(captorLoggingEvent.capture());
        List<ILoggingEvent> values = captorLoggingEvent.getAllValues();
        ILoggingEvent requestLogline = values.get(0);
        ILoggingEvent responseLogline = values.get(1);
        assertThat(requestLogline.getMessage()).isEqualTo("<no request body>");
        assertThat(responseLogline.getMessage()).isEqualTo("<no response body>");
        verifyNoMoreInteractions(mockAppender);
    }

    /**
     * Response with marker with `new byte[n], n > 0` body should be logged.
     */
    @Test
    @SneakyThrows
    public void shouldLogCallWhenInterceptingResponseWithBody() {
        // given
        Mocks mocks = new Mocks();
        when(mocks.execution.execute(any(), any())).thenReturn(mocks.response);
        when(mocks.request.getHeaders()).thenReturn(HttpHeaders.EMPTY);
        when(mocks.request.getURI()).thenReturn(URI.create("https://www.example.com"));
        when(mocks.response.getHeaders()).thenReturn(HttpHeaders.EMPTY);
        when(mocks.response.getBody()).thenReturn(new ByteArrayInputStream("hello.".getBytes()));

        // when
        ClientHttpResponse response = subject.intercept(mocks.request, null, mocks.execution);

        // then
        assertThat(response).isInstanceOf(WrappedClientHttpResponse.class);
        verify(mockAppender, times(2)).doAppend(captorLoggingEvent.capture());
        List<ILoggingEvent> values = captorLoggingEvent.getAllValues();
        ILoggingEvent requestLogline = values.get(0);
        ILoggingEvent responseLogline = values.get(1);
        assertThat(requestLogline.getMessage()).isEqualTo("<no request body>");
        assertThat(responseLogline.getMessage()).isEqualTo("hello.");
        verifyNoMoreInteractions(mockAppender);
    }

    /**
     * Response with squid error.
     */
    @Test
    @SneakyThrows
    public void shouldLogErrorFromProxyWhenIntercepting() {
        // given
        Mocks mocks = new Mocks();
        when(mocks.execution.execute(any(), any())).thenReturn(mocks.response);
        when(mocks.request.getHeaders()).thenReturn(HttpHeaders.EMPTY);
        when(mocks.request.getURI()).thenReturn(URI.create("https://www.example.com"));
        when(mocks.response.getHeaders()).thenReturn(mocks.httpHeadersWithSquidError());
        when(mocks.response.getBody()).thenReturn(new ByteArrayInputStream("hello.".getBytes()));

        // when
        ClientHttpResponse response = subject.intercept(mocks.request, null, mocks.execution);

        // then
        assertThat(response).isInstanceOf(WrappedClientHttpResponse.class);
        verify(mockAppender, times(3)).doAppend(captorLoggingEvent.capture());
        List<ILoggingEvent> values = captorLoggingEvent.getAllValues();
        ILoggingEvent requestLogline = values.get(0);
        ILoggingEvent squidErrorLogline = values.get(1);
        ILoggingEvent responseLogline = values.get(2);
        assertThat(requestLogline.getMessage()).isEqualTo("<no request body>");
        assertThat(squidErrorLogline.getMessage()).contains("The http(s) proxy has returned an error:");
        assertThat(responseLogline.getMessage()).isEqualTo("hello.");
        verifyNoMoreInteractions(mockAppender);
    }

    @Test
    @SneakyThrows
    public void shouldLogCallWhenInterceptingRequestWithBodyWithEnabledRdd() {

        // given
        Mocks mocks = new Mocks();
        when(mocks.execution.execute(any(), any())).thenReturn(mocks.response);
        when(mocks.request.getHeaders()).thenReturn(HttpHeaders.EMPTY);
        when(mocks.request.getURI()).thenReturn(URI.create("https://www.example.com"));
        when(mocks.response.getHeaders()).thenReturn(HttpHeaders.EMPTY);

        // when
        ClientHttpResponse response = subject.intercept(mocks.request, "{\"json\":\"yup\"}".getBytes(), mocks.execution);

        // then
        assertThat(response).isInstanceOf(WrappedClientHttpResponse.class);
        verify(mockAppender, times(2)).doAppend(captorLoggingEvent.capture());
        List<ILoggingEvent> values = captorLoggingEvent.getAllValues();
        ILoggingEvent requestLogline = values.get(0);
        ILoggingEvent responseLogline = values.get(1);
        assertThat(requestLogline.getMessage()).isEqualTo("{\"json\":\"yup\"}");
        assertThat(responseLogline.getMessage()).isEqualTo("<no response body>");
        verifyMarkers(requestLogline, responseLogline);
        verifyNoMoreInteractions(mockAppender);
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("bodiesWithMaskedCounterparts")
    public void shouldLogMaskedResponseBodyWhenItContainsBlacklistedWords(String original, String masked) {
        // given
        Mocks mocks = new Mocks();
        when(mocks.execution.execute(any(), any())).thenReturn(mocks.response);
        when(mocks.request.getHeaders()).thenReturn(HttpHeaders.EMPTY);
        when(mocks.request.getURI()).thenReturn(URI.create("https://www.example.com"));
        when(mocks.response.getHeaders()).thenReturn(HttpHeaders.EMPTY);
        when(mocks.response.getBody()).thenReturn((new ByteArrayInputStream(original.getBytes())));

        // when
        ClientHttpResponse response = subject.intercept(mocks.request, null, mocks.execution);

        // then
        assertThat(response).isInstanceOf(WrappedClientHttpResponse.class);
        verify(mockAppender, times(2)).doAppend(captorLoggingEvent.capture());
        List<ILoggingEvent> values = captorLoggingEvent.getAllValues();
        ILoggingEvent requestLogline = values.get(0);
        ILoggingEvent responseLogline = values.get(1);
        assertThat(requestLogline.getMessage()).isEqualTo("<no request body>");
        assertThat(responseLogline.getMessage()).isEqualTo(masked);
        verifyNoMoreInteractions(mockAppender);
    }

    @SneakyThrows
    @Test
    public void shouldLogLargeResponseBodiesAsTheyAre() {
        // given
        Mocks mocks = new Mocks();
        when(mocks.execution.execute(any(), any())).thenReturn(mocks.response);
        when(mocks.request.getHeaders()).thenReturn(HttpHeaders.EMPTY);
        when(mocks.request.getURI()).thenReturn(URI.create("https://www.example.com"));
        when(mocks.response.getHeaders()).thenReturn(HttpHeaders.EMPTY);
        when(mocks.response.getBody()).thenReturn(new ByteArrayInputStream(LARGE_BODY.getBytes()));

        // when
        ClientHttpResponse response = subject.intercept(mocks.request, null, mocks.execution);

        // then
        assertThat(response).isInstanceOf(WrappedClientHttpResponse.class);
        verify(mockAppender, times(2)).doAppend(captorLoggingEvent.capture());
        List<ILoggingEvent> values = captorLoggingEvent.getAllValues();
        ILoggingEvent requestLogline = values.get(0);
        ILoggingEvent responseLogline = values.get(1);
        assertThat(requestLogline.getMessage()).isEqualTo("<no request body>");
        assertThat(responseLogline.getMessage()).isEqualTo(LARGE_BODY);
        verifyNoMoreInteractions(mockAppender);
    }

    @SneakyThrows
    @Test
    public void shouldLogLargeRequestBodiesAsTheyAre() {
        // given
        Mocks mocks = new Mocks();
        when(mocks.execution.execute(any(), any())).thenReturn(mocks.response);
        when(mocks.request.getHeaders()).thenReturn(HttpHeaders.EMPTY);
        when(mocks.request.getURI()).thenReturn(URI.create("https://www.example.com"));
        when(mocks.response.getHeaders()).thenReturn(HttpHeaders.EMPTY);

        // when
        ClientHttpResponse response = subject.intercept(mocks.request, LARGE_BODY.getBytes(), mocks.execution);

        // then
        assertThat(response).isInstanceOf(WrappedClientHttpResponse.class);
        verify(mockAppender, times(2)).doAppend(captorLoggingEvent.capture());
        List<ILoggingEvent> values = captorLoggingEvent.getAllValues();
        ILoggingEvent requestLogline = values.get(0);
        ILoggingEvent responseLogline = values.get(1);
        assertThat(requestLogline.getMessage()).isEqualTo(LARGE_BODY);
        assertThat(responseLogline.getMessage()).isEqualTo("<no response body>");
        verifyNoMoreInteractions(mockAppender);
    }

    @SneakyThrows
    @Test
    public void shouldLogMessageWithCensoredTokeninUrl() {
        // given
        Mocks mocks = new Mocks();
        when(mocks.execution.execute(any(), any())).thenReturn(mocks.response);
        when(mocks.request.getHeaders()).thenReturn(HttpHeaders.EMPTY);
        when(mocks.request.getURI()).thenReturn(URI.create("https://www.example.com?refreshToken=123myRefreshToken345&grant_type=refresh_token"));
        when(mocks.response.getHeaders()).thenReturn(HttpHeaders.EMPTY);

        // when
        ClientHttpResponse response = subject.intercept(mocks.request, null, mocks.execution);

        // then
        assertThat(response).isInstanceOf(WrappedClientHttpResponse.class);
        verify(mockAppender, times(2)).doAppend(captorLoggingEvent.capture());
        List<ILoggingEvent> values = captorLoggingEvent.getAllValues();
        ILoggingEvent requestLogline = values.get(0);
        assertThat(getMarkersFromLogLine(requestLogline).get("http-uri")).isEqualTo("https://www.example.com?refreshToken=********&grant_type=refresh_token");
        verifyNoMoreInteractions(mockAppender);
    }

    static class Mocks {
        ClientHttpResponse response = Mockito.mock(ClientHttpResponse.class);
        ClientHttpRequestExecution execution = Mockito.mock(ClientHttpRequestExecution.class);
        ClientHttpRequest request = Mockito.mock(ClientHttpRequest.class);

        public HttpHeaders httpHeadersWithSquidError() {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("X-Squid-Error", "dummy squid error");
            return httpHeaders;
        }
    }

    private void verifyMarkers(ILoggingEvent requestLogline, ILoggingEvent responseLogline) {
        String[] reqHttpCallId = requestLogline.getMarker().toString()
                .split(",")[0]
                .split("=");
        String[] respHttpCallId = responseLogline.getMarker().toString()
                .split(",")[0]
                .split("=");

        // Check correlation id (it should match).
        assertThat(reqHttpCallId[0]).isEqualTo("http-call-id");
        assertThat(reqHttpCallId[0]).isEqualTo(respHttpCallId[0]);
        assertThat(reqHttpCallId[1]).isEqualTo(respHttpCallId[1]);

        Map<String, String> requestMarkers = getMarkersFromLogLine(requestLogline);
        assertThat(requestMarkers.keySet()).containsExactlyInAnyOrder(
                "http-headers",
                "http-is-request",
                "http-method",
                "http-host",
                "http-uri",
                "raw-data",
                "raw-data-type",
                "raw-data-call-id"
        );

        Map<String, String> responseMarkers = getMarkersFromLogLine(responseLogline);
        assertThat(responseMarkers.keySet()).containsExactlyInAnyOrder(
                "http-headers",
                "http-is-response",
                "http-status-code",
                "http-call-duration-ms",
                "raw-data",
                "raw-data-type",
                "raw-data-call-id"
        );

        assertThat(requestMarkers.get("raw-data")).isEqualTo(String.valueOf(true));
        assertThat(responseMarkers.get("raw-data")).isEqualTo(String.valueOf(true));
    }

    private Map<String, String> getMarkersFromLogLine(ILoggingEvent logLine) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(logLine.getMarker().iterator(), Spliterator.ORDERED), false)
                .map(Objects::toString)
                .map(s -> s.split("=", 2))
                .collect(Collectors.toMap(L -> L[0], L -> L[1]));
    }

    private static Stream<Arguments> bodiesWithMaskedCounterparts() {
        return Stream.of(
                Arguments.of("{\"accessToken\":\"access_token_that_should_not_be_visible\"}", "{\"accessToken\":\"********\"}"),
                Arguments.of("{\"refreshToken\":\"refresh_token_that_should_not_be_visible\"}", "{\"refreshToken\":\"********\"}"),
                Arguments.of("{\"accessToken\":\"access_token_that_should_not_be_visible\",\"refreshToken\":\"refresh_token_that_should_not_be_visible\",\"expires_in\":\"3600\",\"type\":\"Bearer\"}", "{\"accessToken\":\"********\",\"refreshToken\":\"********\",\"expires_in\":\"3600\",\"type\":\"Bearer\"}"),
                Arguments.of("refresh_token=refresh_token_that_should_not_be_visible&grant_type=refresh_token", "refresh_token=********&grant_type=refresh_token"),
                Arguments.of("grant_type=refresh_token&refresh_token=refresh_token_that_should_not_be_visible", "grant_type=refresh_token&refresh_token=********"),
                Arguments.of("username=johndoe&password=secret", "username=********&password=********")
        );
    }
}