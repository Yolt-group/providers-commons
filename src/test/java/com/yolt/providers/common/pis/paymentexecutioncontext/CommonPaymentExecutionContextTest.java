package com.yolt.providers.common.pis.paymentexecutioncontext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yolt.providers.common.pis.paymentexecutioncontext.errorhandler.PaymentExecutionContextErrorHandler;
import com.yolt.providers.common.pis.paymentexecutioncontext.errorhandler.PaymentExecutionContextPreExecutionErrorHandler;
import com.yolt.providers.common.pis.paymentexecutioncontext.exception.PaymentExecutionTechnicalException;
import com.yolt.providers.common.pis.paymentexecutioncontext.exception.ResponseBodyValidationException;
import com.yolt.providers.common.pis.paymentexecutioncontext.http.PaymentExecutionHttpHeadersProvider;
import com.yolt.providers.common.pis.paymentexecutioncontext.http.PaymentExecutionHttpRequestBodyProvider;
import com.yolt.providers.common.pis.paymentexecutioncontext.http.PaymentHttpRequestInvoker;
import com.yolt.providers.common.pis.paymentexecutioncontext.model.*;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class CommonPaymentExecutionContextTest {

    private CommonPaymentExecutionContext<String, String, String> commonPaymentExecutionContext;

    @Mock
    private PaymentExecutionHttpRequestBodyProvider<String, String> httpRequestBodyProvider;

    @Mock
    private PaymentExecutionHttpHeadersProvider<String, String> httpHeadersProvider;

    @Mock
    private PaymentHttpRequestInvoker<String, String> paymentHttpRequestInvoker;

    @Mock
    private PaymentExecutionResponseBodyValidator<String> paymentExecutionResponseBodyValidator;

    @Mock
    private PaymentStatusesExtractor<String, String> paymentStatusesExtractor;

    @Mock
    private PaymentExecutionContextErrorHandler paymentExecutionContextErrorHandler;

    @Mock
    private PaymentExecutionContextPreExecutionErrorHandler preExecutionErrorHandler;

    @Mock
    private PaymentPreExecutionResultProvider<String> preExecutionResultProvider;

    @Mock
    private ObjectMapper objectMapper;

    private final Clock clock = Clock.fixed(Instant.parse("2021-01-01T00:00:00Z"), ZoneOffset.UTC);

    @Captor
    private ArgumentCaptor<HttpEntity<String>> httpEntityArgumentCaptor;

    @BeforeEach
    void beforeEach() {
        given(objectMapper.configure(any(DeserializationFeature.class), anyBoolean()))
                .willReturn(objectMapper);
        commonPaymentExecutionContext = new CommonPaymentExecutionContext<>(
                httpRequestBodyProvider,
                httpHeadersProvider,
                paymentHttpRequestInvoker,
                paymentExecutionResponseBodyValidator,
                paymentStatusesExtractor,
                preExecutionErrorHandler,
                paymentExecutionContextErrorHandler,
                String.class,
                objectMapper,
                clock
        );
    }

    @Test
    void shouldReturnPaymentExecutionResultWithAllNecessaryFieldsWhenEverythingWentWell() throws JsonProcessingException, ResponseBodyValidationException {
        // given
        given(preExecutionResultProvider.providePreExecutionResult())
                .willReturn("fakePreExecutionResult");
        given(httpRequestBodyProvider.provideHttpRequestBody(anyString()))
                .willReturn("fakeRequestBody");
        HttpHeaders fakeHttpHeaders = new HttpHeaders();
        given(httpHeadersProvider.provideHttpHeaders(anyString(), anyString()))
                .willReturn(fakeHttpHeaders);
        JsonNode fakeRawBody = mock(JsonNode.class);
        given(paymentHttpRequestInvoker.invokeRequest(any(HttpEntity.class), anyString()))
                .willReturn(ResponseEntity.ok(fakeRawBody));
        given(objectMapper.treeToValue(any(JsonNode.class), eq(String.class)))
                .willReturn("fakeResponseBody");
        given(objectMapper.writeValueAsString(anyString()))
                .willReturn("fakeRawRequestBody");
        given(objectMapper.writeValueAsString(any(JsonNode.class)))
                .willReturn("fakeRawResponseBody");
        PaymentStatuses fakePaymentStatuses = new PaymentStatuses(RawBankPaymentStatus.forStatus("bank status", "ok"), EnhancedPaymentStatus.ACCEPTED);
        given(paymentStatusesExtractor.extractPaymentStatuses(anyString(), anyString()))
                .willReturn(fakePaymentStatuses);

        // when
        PaymentExecutionResult<String, String> result = commonPaymentExecutionContext.execute(preExecutionResultProvider);

        // then
        then(preExecutionResultProvider)
                .should()
                .providePreExecutionResult();
        then(httpRequestBodyProvider)
                .should()
                .provideHttpRequestBody("fakePreExecutionResult");
        then(httpHeadersProvider)
                .should()
                .provideHttpHeaders("fakePreExecutionResult", "fakeRequestBody");
        then(paymentHttpRequestInvoker)
                .should()
                .invokeRequest(httpEntityArgumentCaptor.capture(), eq("fakePreExecutionResult"));
        HttpEntity<String> capturedHttpEntity = httpEntityArgumentCaptor.getValue();
        assertThat(capturedHttpEntity).extracting(HttpEntity::getBody, HttpEntity::getHeaders)
                .contains("fakeRequestBody", fakeHttpHeaders);
        then(objectMapper)
                .should()
                .treeToValue(fakeRawBody, String.class);
        then(paymentExecutionResponseBodyValidator)
                .should()
                .validate("fakeResponseBody", fakeRawBody);
        then(objectMapper)
                .should()
                .writeValueAsString("fakeRequestBody");
        then(objectMapper)
                .should()
                .writeValueAsString(fakeRawBody);
        then(paymentStatusesExtractor)
                .should()
                .extractPaymentStatuses("fakeResponseBody", "fakePreExecutionResult");
        assertThat(result).extracting(
                PaymentExecutionResult::getRequestTimestamp,
                PaymentExecutionResult::getResponseTimestamp,
                PaymentExecutionResult::getRawRequestBody,
                PaymentExecutionResult::getRawResponseBody,
                PaymentExecutionResult::getRawRequestHeaders,
                PaymentExecutionResult::getRawResponseHeaders,
                PaymentExecutionResult::getPaymentStatuses,
                PaymentExecutionResult::getHttpResponseBody,
                PaymentExecutionResult::getPreExecutionResult
        ).contains(
                Instant.now(clock),
                Instant.now(clock),
                "fakeRawRequestBody",
                "fakeRawResponseBody",
                "[]",
                "[]",
                fakePaymentStatuses,
                Optional.of("fakeResponseBody"),
                "fakePreExecutionResult"
        );
    }

    @Test
    void shouldReturnPaymentExecutionResultWhenNonTechnicalExceptionOccursForInvokeRequest() {
        // given
        given(preExecutionResultProvider.providePreExecutionResult())
                .willReturn("fakePreExecutionResult");
        given(httpRequestBodyProvider.provideHttpRequestBody(anyString()))
                .willReturn("fakeRequestBody");
        HttpHeaders fakeHttpHeaders = new HttpHeaders();
        given(httpHeadersProvider.provideHttpHeaders(anyString(), anyString()))
                .willReturn(fakeHttpHeaders);
        JsonNode fakeRawBody = mock(JsonNode.class);
        given(paymentHttpRequestInvoker.invokeRequest(any(HttpEntity.class), anyString()))
                .willThrow(RuntimeException.class);
        PaymentStatuses fakePaymentStatuses = new PaymentStatuses(RawBankPaymentStatus.forStatus("bank status", "failed"), EnhancedPaymentStatus.REJECTED);
        given(paymentExecutionContextErrorHandler.handleError(any(HttpEntity.class), any(Instant.class), any(Exception.class)))
                .willReturn(new PaymentExecutionResult<String, String>(
                        Instant.now(clock),
                        Instant.now(clock),
                        "fakeRawRequestBody",
                        "fakeRawResponseBody",
                        "[]",
                        "[]",
                        fakePaymentStatuses,
                        null,
                        null
                ));

        // when
        PaymentExecutionResult<String, String> result = commonPaymentExecutionContext.execute(preExecutionResultProvider);

        // then
        assertThat(result).extracting(
                PaymentExecutionResult::getRequestTimestamp,
                PaymentExecutionResult::getResponseTimestamp,
                PaymentExecutionResult::getRawRequestBody,
                PaymentExecutionResult::getRawResponseBody,
                PaymentExecutionResult::getRawRequestHeaders,
                PaymentExecutionResult::getRawResponseHeaders,
                PaymentExecutionResult::getPaymentStatuses,
                PaymentExecutionResult::getHttpResponseBody,
                PaymentExecutionResult::getPreExecutionResult
        ).contains(
                Instant.now(clock),
                Instant.now(clock),
                "fakeRawRequestBody",
                "fakeRawResponseBody",
                "[]",
                "[]",
                fakePaymentStatuses,
                Optional.empty(),
                null
        );
    }

    @Test
    void shouldReturnPreExecutionResultWithProperStatusForExecuteWhenAnyExceptionIsThrownInsidePreparationFlowAndPreExecutionErrorHandlerHandlesThatError() {
        // given
        given(preExecutionResultProvider.providePreExecutionResult())
                .willThrow(RuntimeException.class);
        given(preExecutionErrorHandler.handleError(any(Exception.class)))
                .willReturn(new PaymentExecutionResult<>(Instant.now(clock),
                        Instant.now(clock),
                        "",
                        "",
                        "[]",
                        "[]",
                        new PaymentStatuses(RawBankPaymentStatus.unknown(), EnhancedPaymentStatus.REJECTED),
                        null,
                        null));

        // when
        PaymentExecutionResult<String, String> result = commonPaymentExecutionContext.execute(preExecutionResultProvider);

        // then
        assertThat(result.getHttpResponseBody()).isEmpty();
        assertThat(result.getPreExecutionResult()).isNull();
        assertThat(result.getPaymentStatuses()).satisfies(statuses -> {
            assertThat(statuses.getRawBankPaymentStatus().getStatus()).isEqualTo("UNKNOWN");
            assertThat(statuses.getRawBankPaymentStatus().getReason()).isEmpty();
            assertThat(statuses.getPaymentStatus()).isEqualTo(EnhancedPaymentStatus.REJECTED);
        });
    }

    @Test
    void shouldThrowPaymentExecutionTechnicalExceptionForExecuteWhenAnyExceptionIsThrownInsidePreparationFlowAndPreExecutionErrorHandlerWrapsThatErrorInsidePaymentExecutionTechnicalException() {
        // given
        given(preExecutionResultProvider.providePreExecutionResult())
                .willThrow(RuntimeException.class);
        given(preExecutionErrorHandler.handleError(any(Exception.class)))
                .willThrow(PaymentExecutionTechnicalException.class);

        // when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> commonPaymentExecutionContext.execute(preExecutionResultProvider);

        // then
        assertThatExceptionOfType(PaymentExecutionTechnicalException.class)
                .isThrownBy(throwingCallable);
    }


    @Test
    void shouldThrowPaymentExecutionTechnicalExceptionForExecuteWhenAnyExceptionIsThrownDuringStatusesExtractingAndPreExecutionErrorHandlerWrapsThatErrorInsidePaymentExecutionTechnicalException() throws JsonProcessingException, ResponseBodyValidationException {
        // given
        given(preExecutionResultProvider.providePreExecutionResult())
                .willReturn("fakePreExecutionResult");
        given(httpRequestBodyProvider.provideHttpRequestBody(anyString()))
                .willReturn("fakeRequestBody");
        HttpHeaders fakeHttpHeaders = new HttpHeaders();
        given(httpHeadersProvider.provideHttpHeaders(anyString(), anyString()))
                .willReturn(fakeHttpHeaders);
        JsonNode fakeRawBody = mock(JsonNode.class);
        given(paymentHttpRequestInvoker.invokeRequest(any(HttpEntity.class), anyString()))
                .willReturn(ResponseEntity.ok(fakeRawBody));
        given(objectMapper.treeToValue(any(JsonNode.class), eq(String.class)))
                .willReturn("fakeResponseBody");
        given(objectMapper.writeValueAsString(anyString()))
                .willReturn("fakeRawRequestBody");
        given(objectMapper.writeValueAsString(any(JsonNode.class)))
                .willReturn("fakeRawResponseBody");
        given(paymentStatusesExtractor.extractPaymentStatuses(anyString(), anyString()))
                .willThrow(NullPointerException.class);
        given(preExecutionErrorHandler.handleError(any(NullPointerException.class)))
                .willThrow(PaymentExecutionTechnicalException.class);

        // when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> commonPaymentExecutionContext.execute(preExecutionResultProvider);

        // then
        assertThatExceptionOfType(PaymentExecutionTechnicalException.class)
                .isThrownBy(throwingCallable);
    }
}
