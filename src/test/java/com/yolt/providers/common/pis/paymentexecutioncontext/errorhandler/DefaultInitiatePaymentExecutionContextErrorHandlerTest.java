package com.yolt.providers.common.pis.paymentexecutioncontext.errorhandler;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yolt.providers.common.exception.ConfirmationFailedException;
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
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefaultInitiatePaymentExecutionContextErrorHandlerTest {

    private DefaultInitiatePaymentExecutionContextErrorHandler initiatePaymentExecutionErrorHandler;

    @Mock
    private RawBankPaymentStatusMapper rawBankPaymentStatusMapper;

    @Mock
    private ObjectMapper objectMapper;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    protected Appender<ILoggingEvent> mockAppender;
    protected ArgumentCaptor<ILoggingEvent> captorLoggingEvent;

    @BeforeEach
    public void beforeEach() {
        Clock clock = Clock.systemDefaultZone();
        initiatePaymentExecutionErrorHandler = new DefaultInitiatePaymentExecutionContextErrorHandler(
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
    public void shouldReturnPecResultWithExecutionFailedStatusWhenUnexpectedErrorIsProvided() throws JsonProcessingException {
        //given
        HttpEntity<String> httpEntity = prepareHttpEntity();
        Instant requestTimestamp = Instant.now();
        CreationFailedException exception = new CreationFailedException("Creation failed");

        when(objectMapper.writeValueAsString(any())).thenReturn("parsed body");

        //when
        PaymentExecutionResult<Object, Void> result = initiatePaymentExecutionErrorHandler.handleError(httpEntity, requestTimestamp, exception);

        //then
        assertThat(result.getPaymentStatuses()).extracting(
                        PaymentStatuses::getPaymentStatus,
                        (paymentStatuses) -> paymentStatuses.getRawBankPaymentStatus().getStatus(),
                        (paymentStatuses) -> paymentStatuses.getRawBankPaymentStatus().getReason())
                .contains(EnhancedPaymentStatus.INITIATION_ERROR, "UNKNOWN", "");
        verify(objectMapper).writeValueAsString("request body");

    }

    @Test
    public void shouldLogStackTraceForNonHttpStatusCode() throws JsonProcessingException {
        //given
        HttpEntity<String> httpEntity = prepareHttpEntity();
        Instant requestTimestamp = Instant.now();
        CreationFailedException exception = new CreationFailedException("Creation failed");

        when(objectMapper.writeValueAsString(any())).thenReturn("parsed body");

        //when
        initiatePaymentExecutionErrorHandler.handleError(httpEntity, requestTimestamp, exception);

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

    @Test
    public void shouldReturnPecResultWithInitiationErrorHttpStatusCodeExceptionIsProvided() throws JsonProcessingException {
        //given
        HttpEntity<String> httpEntity = prepareHttpEntity();
        Instant requestTimestamp = Instant.now();
        String responseBodyAsString = "response";
        HttpClientErrorException exception = HttpClientErrorException.create(HttpStatus.NOT_FOUND,
                "Not found",
                HttpHeaders.EMPTY,
                responseBodyAsString.getBytes(),
                StandardCharsets.UTF_8);

        when(rawBankPaymentStatusMapper.evaluate(anyString())).thenReturn(RawBankPaymentStatus.unknown());
        when(objectMapper.writeValueAsString(any())).thenReturn("parsed secondary");

        //when
        PaymentExecutionResult<Object, Void> result = initiatePaymentExecutionErrorHandler.handleError(httpEntity, requestTimestamp, exception);

        //then
        assertThat(result.getPaymentStatuses()).extracting(
                        PaymentStatuses::getPaymentStatus,
                        (paymentStatuses) -> paymentStatuses.getRawBankPaymentStatus().getStatus(),
                        (paymentStatuses) -> paymentStatuses.getRawBankPaymentStatus().getReason())
                .contains(EnhancedPaymentStatus.INITIATION_ERROR, "UNKNOWN", "");

        verify(objectMapper).writeValueAsString(stringArgumentCaptor.capture());
        String capturedValue = stringArgumentCaptor.getValue();
        assertThat(capturedValue).isEqualTo("request body");
        verify(rawBankPaymentStatusMapper).evaluate("response");
    }

    @Test
    public void shouldReturnPecResultWithInitiationErrorHttpStatusCodeExceptionIsProvidedAsCause() throws JsonProcessingException {
        //given
        HttpEntity<String> httpEntity = prepareHttpEntity();
        Instant requestTimestamp = Instant.now();
        String responseBodyAsString = "response";
        HttpClientErrorException exception = HttpClientErrorException.create(HttpStatus.NOT_FOUND,
                "Not found",
                HttpHeaders.EMPTY,
                responseBodyAsString.getBytes(),
                StandardCharsets.UTF_8);
        ConfirmationFailedException rootException = new ConfirmationFailedException("Root exception", exception);

        when(rawBankPaymentStatusMapper.evaluate(anyString())).thenReturn(RawBankPaymentStatus.unknown());
        when(objectMapper.writeValueAsString(any())).thenReturn("parsed secondary");

        //when
        PaymentExecutionResult<Object, Void> result = initiatePaymentExecutionErrorHandler.handleError(httpEntity, requestTimestamp, rootException);

        //then
        assertThat(result.getPaymentStatuses()).extracting(
                        PaymentStatuses::getPaymentStatus,
                        (paymentStatuses) -> paymentStatuses.getRawBankPaymentStatus().getStatus(),
                        (paymentStatuses) -> paymentStatuses.getRawBankPaymentStatus().getReason())
                .contains(EnhancedPaymentStatus.INITIATION_ERROR, "UNKNOWN", "");

        verify(objectMapper).writeValueAsString(stringArgumentCaptor.capture());
        String capturedValue = stringArgumentCaptor.getValue();
        assertThat(capturedValue).isEqualTo("request body");
        verify(rawBankPaymentStatusMapper).evaluate("response");
    }

    @Test
    public void shouldReturnPecResultWithInitiationErrorStatusWhenResponseBodyValidationExceptionIsProvided() throws JsonProcessingException {
        //given
        HttpEntity<String> httpEntity = prepareHttpEntity();
        Instant requestTimestamp = Instant.now();
        JsonNode rawResponseBody = mock(JsonNode.class);
        ResponseBodyValidationException exception = new ResponseBodyValidationException(rawResponseBody, "Message");

        when(rawBankPaymentStatusMapper.evaluate(anyString())).thenReturn(RawBankPaymentStatus.unknown());
        when(objectMapper.writeValueAsString(any())).thenReturn("response body", "parsed body");

        //when
        PaymentExecutionResult<Object, Void> result = initiatePaymentExecutionErrorHandler.handleError(httpEntity, requestTimestamp, exception);

        //then
        assertThat(result.getPaymentStatuses()).extracting(
                        PaymentStatuses::getPaymentStatus,
                        (paymentStatuses) -> paymentStatuses.getRawBankPaymentStatus().getStatus(),
                        (paymentStatuses) -> paymentStatuses.getRawBankPaymentStatus().getReason())
                .contains(EnhancedPaymentStatus.INITIATION_ERROR, "UNKNOWN", "");

        verify(objectMapper).writeValueAsString("request body");
        verify(rawBankPaymentStatusMapper).evaluate("response body");
    }

    @Test
    public void shouldReturnPecResultWithInitiationErrorStatusWhenSocketTimeoutExceptionIsProvidedAsCause() throws JsonProcessingException {
        //given
        HttpEntity<String> httpEntity = prepareHttpEntity();
        Instant requestTimestamp = Instant.now();
        CreationFailedException exception = new CreationFailedException("Creation failed", new SocketTimeoutException());

        when(objectMapper.writeValueAsString(any())).thenReturn("parsed body");

        //when
        PaymentExecutionResult<Object, Void> result = initiatePaymentExecutionErrorHandler.handleError(httpEntity, requestTimestamp, exception);

        //then
        assertThat(result.getPaymentStatuses()).extracting(
                        PaymentStatuses::getPaymentStatus,
                        (paymentStatuses) -> paymentStatuses.getRawBankPaymentStatus().getStatus(),
                        (paymentStatuses) -> paymentStatuses.getRawBankPaymentStatus().getReason())
                .contains(EnhancedPaymentStatus.INITIATION_ERROR, "UNKNOWN", "");
        verify(objectMapper).writeValueAsString("request body");
    }

    private HttpEntity<String> prepareHttpEntity() {
        return new HttpEntity<>("request body");
    }
}
