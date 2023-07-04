package com.yolt.providers.common.pis.paymentexecutioncontext.errorhandler;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yolt.providers.common.exception.CreationFailedException;
import com.yolt.providers.common.pis.paymentexecutioncontext.exception.ResponseBodyValidationException;
import com.yolt.providers.common.pis.paymentexecutioncontext.model.EnhancedPaymentStatus;
import com.yolt.providers.common.pis.paymentexecutioncontext.model.PaymentExecutionResult;
import com.yolt.providers.common.pis.paymentexecutioncontext.model.PaymentStatuses;
import com.yolt.providers.common.pis.paymentexecutioncontext.model.RawBankPaymentStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultGetPaymentStatusExecutionContextErrorHandlerTest {

    private DefaultGetPaymentStatusExecutionContextErrorHandler getPaymentStatusExecutionContextErrorHandler;

    @Mock
    private RawBankPaymentStatusMapper rawBankPaymentStatusMapper;

    @Mock
    private ObjectMapper objectMapper;

    protected Appender<ILoggingEvent> mockAppender;
    protected ArgumentCaptor<ILoggingEvent> captorLoggingEvent;

    @BeforeEach
    public void beforeEach() {
        Clock clock = Clock.systemDefaultZone();
        getPaymentStatusExecutionContextErrorHandler = new DefaultGetPaymentStatusExecutionContextErrorHandler(
                rawBankPaymentStatusMapper,
                objectMapper,
                clock);

        mockAppender = mock(Appender.class);
        captorLoggingEvent = ArgumentCaptor.forClass(ILoggingEvent.class);
        final Logger logger = (Logger) LoggerFactory.getLogger(PaymentExecutionContextLoggingUtil.class);
        logger.setLevel(Level.ALL);
        logger.addAppender(mockAppender);
    }

    @AfterEach
    public void afterEach() {
        final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.detachAppender(mockAppender);
    }

    @Test
    public void shouldReturnPecResultWithUnknownStatusWhenUnexpectedExceptionIsProvided() throws JsonProcessingException {
        //given
        HttpEntity<String> httpEntity = prepareHttpEntity();
        Instant requestTimestamp = Instant.now();
        CreationFailedException exception = new CreationFailedException("Creation failed");

        when(objectMapper.writeValueAsString(any())).thenReturn("parsed body");

        //when
        PaymentExecutionResult<Object, Void> result = getPaymentStatusExecutionContextErrorHandler.handleError(httpEntity, requestTimestamp, exception);

        //then
        assertThat(result.getPaymentStatuses()).extracting(
                        PaymentStatuses::getPaymentStatus,
                        (paymentStatuses) -> paymentStatuses.getRawBankPaymentStatus().getStatus(),
                        (paymentStatuses) -> paymentStatuses.getRawBankPaymentStatus().getReason())
                .contains(EnhancedPaymentStatus.UNKNOWN, "UNKNOWN", "");
        verify(objectMapper).writeValueAsString("request body");
    }

    @Test
    public void shouldReturnPecResultWithUnknownStatusWhenHttpStatusCodeExceptionIsProvided() throws JsonProcessingException {
        //given
        HttpEntity<String> httpEntity = prepareHttpEntity();
        Instant requestTimestamp = Instant.now();
        String responseBodyAsString = "response";
        HttpServerErrorException exception = HttpServerErrorException.create(HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error",
                HttpHeaders.EMPTY,
                responseBodyAsString.getBytes(),
                StandardCharsets.UTF_8);

        when(rawBankPaymentStatusMapper.evaluate(anyString())).thenReturn(RawBankPaymentStatus.unknown());
        when(objectMapper.writeValueAsString(any())).thenReturn("parsed body");

        //when
        PaymentExecutionResult<Object, Void> result = getPaymentStatusExecutionContextErrorHandler.handleError(httpEntity, requestTimestamp, exception);

        //then
        assertThat(result.getPaymentStatuses()).extracting(
                        PaymentStatuses::getPaymentStatus,
                        (paymentStatuses) -> paymentStatuses.getRawBankPaymentStatus().getStatus(),
                        (paymentStatuses) -> paymentStatuses.getRawBankPaymentStatus().getReason())
                .contains(EnhancedPaymentStatus.UNKNOWN, "UNKNOWN", "");

        verify(objectMapper).writeValueAsString("request body");
        verify(rawBankPaymentStatusMapper).evaluate(responseBodyAsString);
    }

    @Test
    public void shouldReturnPecResultWithUnknownStatusWhenResponseBodyValidationExceptionIsProvided() throws JsonProcessingException {
        //given
        HttpEntity<String> httpEntity = prepareHttpEntity();
        Instant requestTimestamp = Instant.now();
        JsonNode rawBody = mock(JsonNode.class);
        ResponseBodyValidationException exception = new ResponseBodyValidationException(rawBody, "Message");

        when(rawBankPaymentStatusMapper.evaluate(anyString())).thenReturn(RawBankPaymentStatus.unknown());
        when(objectMapper.writeValueAsString(any())).thenReturn("raw body");

        //when
        PaymentExecutionResult<Object, Void> result = getPaymentStatusExecutionContextErrorHandler.handleError(httpEntity, requestTimestamp, exception);

        //then
        assertThat(result.getPaymentStatuses()).extracting(
                        PaymentStatuses::getPaymentStatus,
                        (paymentStatuses) -> paymentStatuses.getRawBankPaymentStatus().getStatus(),
                        (paymentStatuses) -> paymentStatuses.getRawBankPaymentStatus().getReason())
                .contains(EnhancedPaymentStatus.UNKNOWN, "UNKNOWN", "");

        verify(objectMapper).writeValueAsString(rawBody);
        verify(objectMapper).writeValueAsString("request body");
        verify(rawBankPaymentStatusMapper).evaluate("raw body");
    }

    @Test
    public void shouldLogStackTraceForNonHttpStatusCode() throws JsonProcessingException {
        //given
        HttpEntity<String> httpEntity = prepareHttpEntity();
        Instant requestTimestamp = Instant.now();
        CreationFailedException exception = new CreationFailedException("Creation failed");

        when(objectMapper.writeValueAsString(any())).thenReturn("parsed body");

        //when
        getPaymentStatusExecutionContextErrorHandler.handleError(httpEntity, requestTimestamp, exception);

        //then
        verify(mockAppender, times(2)).doAppend(captorLoggingEvent.capture());
        List<ILoggingEvent> values = captorLoggingEvent.getAllValues();
        ILoggingEvent exceptionsRDDLogLine = values.get(0);
        assertThat(exceptionsRDDLogLine.getMessage()).isEqualTo("Unexpected error occurred during payment. Check RDD for full stacktrace");
        ILoggingEvent exceptionStackTraceLogLine = values.get(1);
        assertThat(exceptionStackTraceLogLine.getMessage()).contains("StackTrace: ");
        assertThat(exceptionStackTraceLogLine.getMarker().toString())
                .contains("raw-data")
                .contains("true")
                .contains("raw-data-type")
                .contains("MRDD");

    }

    private HttpEntity<String> prepareHttpEntity() {
        return new HttpEntity<>("request body");
    }
}