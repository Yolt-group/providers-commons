package com.yolt.providers.common.pis.paymentexecutioncontext.adapter.uk.initiate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yolt.providers.common.pis.common.PaymentType;
import com.yolt.providers.common.pis.paymentexecutioncontext.CommonPaymentExecutionContext;
import com.yolt.providers.common.pis.paymentexecutioncontext.exception.PaymentExecutionTechnicalException;
import com.yolt.providers.common.pis.paymentexecutioncontext.model.*;
import com.yolt.providers.common.pis.uk.InitiateUkDomesticPaymentRequestBuilder;
import com.yolt.providers.common.pis.ukdomestic.InitiateUkDomesticPaymentRequest;
import com.yolt.providers.common.pis.ukdomestic.InitiateUkDomesticPaymentResponseDTO;
import com.yolt.providers.common.pis.ukdomestic.UkProviderState;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class CommonUkInitiatePaymentExecutionContextAdapterTest {

    @InjectMocks
    private CommonUkInitiatePaymentExecutionContextAdapter<InitiateUkDomesticPaymentRequest, String, String, String> ukInitiatePaymentExecutionContextAdapter;

    @Mock
    private CommonPaymentExecutionContext<String, String, String> ukInitiatePaymentExecutionContext;

    @Mock
    private PaymentPreExecutionResultMapper<InitiateUkDomesticPaymentRequest, String> ukDomesticInitiatePaymentPreExecutionResultMapper;

    @Mock
    private PaymentAuthorizationUrlExtractor<String, String> authorizationUrlExtractor;

    @Mock
    private UkPaymentProviderStateExtractor<String, String> ukPaymentProviderStateExtractor;

    @Mock
    private ObjectMapper objectMapper;

    @Captor
    private ArgumentCaptor<PaymentPreExecutionResultProvider<String>> paymentPreExecutionResultProviderArgumentCaptor;

    @Test
    void shouldReturnInitiateUkDomesticPaymentResponseDTOWithAuthUrlProviderStateAndPecMetadataForInitiatePaymentWithCorrectData() throws JsonProcessingException {
        // given
        InitiateUkDomesticPaymentRequest fakeInitiateUkDomesticPaymentRequest = new InitiateUkDomesticPaymentRequestBuilder()
                .build();
        PaymentExecutionResult<String, String> fakePecResult = new PaymentExecutionResult<>(
                Instant.now(),
                Instant.now(),
                "",
                "",
                "",
                "",
                new PaymentStatuses(RawBankPaymentStatus.unknown(), EnhancedPaymentStatus.ACCEPTED),
                "fakeResponseBody",
                "fakePreExecutionResult");
        given(ukInitiatePaymentExecutionContext.execute(any()))
                .willReturn(fakePecResult);
        given(ukDomesticInitiatePaymentPreExecutionResultMapper.map(any(InitiateUkDomesticPaymentRequest.class)))
                .willReturn("fakePreExecutionResult");
        given(authorizationUrlExtractor.extractAuthorizationUrl(anyString(), anyString()))
                .willReturn("fakeAuthUrl");
        UkProviderState fakeUkProviderState = new UkProviderState("fakeConsentId", PaymentType.SINGLE, null);
        given(ukPaymentProviderStateExtractor.extractUkProviderState(anyString(), anyString()))
                .willReturn(fakeUkProviderState);
        given(objectMapper.writeValueAsString(any(UkProviderState.class)))
                .willReturn("fakeUkProviderState");

        // when
        InitiateUkDomesticPaymentResponseDTO result = ukInitiatePaymentExecutionContextAdapter.initiatePayment(fakeInitiateUkDomesticPaymentRequest);

        // then
        then(ukInitiatePaymentExecutionContext)
                .should()
                .execute(paymentPreExecutionResultProviderArgumentCaptor.capture());
        PaymentPreExecutionResultProvider<String> preExecutionResultProvider = paymentPreExecutionResultProviderArgumentCaptor.getValue();
        String preExecutionResult = preExecutionResultProvider.providePreExecutionResult();
        assertThat(preExecutionResult).isEqualTo("fakePreExecutionResult");
        then(ukDomesticInitiatePaymentPreExecutionResultMapper)
                .should()
                .map(fakeInitiateUkDomesticPaymentRequest);
        then(authorizationUrlExtractor)
                .should()
                .extractAuthorizationUrl("fakeResponseBody", "fakePreExecutionResult");
        then(ukPaymentProviderStateExtractor)
                .should()
                .extractUkProviderState("fakeResponseBody", "fakePreExecutionResult");
        then(objectMapper)
                .should()
                .writeValueAsString(fakeUkProviderState);
        assertThat(result)
                .extracting(
                        InitiateUkDomesticPaymentResponseDTO::getLoginUrl,
                        InitiateUkDomesticPaymentResponseDTO::getProviderState,
                        InitiateUkDomesticPaymentResponseDTO::getPaymentExecutionContextMetadata
                )
                .contains(
                        "fakeAuthUrl",
                        "fakeUkProviderState",
                        fakePecResult.toMetadata()
                );
    }

    @Test
    void shouldThrowPaymentExecutionTechnicalExceptionForInitiatePaymentWhenAnyExceptionIsThrownUponResponseBodyHandling() throws JsonProcessingException {
        // given
        InitiateUkDomesticPaymentRequest fakeInitiateUkDomesticPaymentRequest = new InitiateUkDomesticPaymentRequestBuilder()
                .build();
        PaymentExecutionResult<String, String> fakePecResult = new PaymentExecutionResult<>(
                Instant.now(),
                Instant.now(),
                "",
                "",
                "",
                "",
                new PaymentStatuses(RawBankPaymentStatus.unknown(), EnhancedPaymentStatus.ACCEPTED),
                "fakeResponseBody",
                "fakePreExecutionResult");
        given(ukInitiatePaymentExecutionContext.execute(any()))
                .willReturn(fakePecResult);
        given(authorizationUrlExtractor.extractAuthorizationUrl(anyString(), anyString()))
                .willReturn("fakeAuthUrl");
        UkProviderState fakeUkProviderState = new UkProviderState("fakeConsentId", PaymentType.SINGLE, null);
        given(ukPaymentProviderStateExtractor.extractUkProviderState(anyString(), anyString()))
                .willReturn(fakeUkProviderState);
        given(objectMapper.writeValueAsString(any(UkProviderState.class)))
                .willThrow(JsonProcessingException.class);

        // when
        ThrowableAssert.ThrowingCallable callable = () -> ukInitiatePaymentExecutionContextAdapter.initiatePayment(fakeInitiateUkDomesticPaymentRequest);

        // then
        assertThatExceptionOfType(PaymentExecutionTechnicalException.class)
                .isThrownBy(callable)
                .withCauseInstanceOf(JsonProcessingException.class);
    }

    @Test
    void shouldReturnInitiateUkDomesticPaymentResponseDTOWithEmptyLoginUrlEmptyProviderStateAndPecMetadataForInitiatePaymentWhenHttpResponseBodyIsNotAvailableInPecResult() {
        // given
        InitiateUkDomesticPaymentRequest fakeInitiateUkDomesticPaymentRequest = new InitiateUkDomesticPaymentRequestBuilder()
                .build();
        PaymentExecutionResult<String, String> fakePecResult = new PaymentExecutionResult<>(
                Instant.now(),
                Instant.now(),
                "",
                "",
                "",
                "",
                new PaymentStatuses(RawBankPaymentStatus.unknown(), EnhancedPaymentStatus.ACCEPTED),
                null,
                null);
        given(ukInitiatePaymentExecutionContext.execute(any()))
                .willReturn(fakePecResult);

        // when
        InitiateUkDomesticPaymentResponseDTO result = ukInitiatePaymentExecutionContextAdapter.initiatePayment(fakeInitiateUkDomesticPaymentRequest);

        // then
        assertThat(result)
                .extracting(
                        InitiateUkDomesticPaymentResponseDTO::getLoginUrl,
                        InitiateUkDomesticPaymentResponseDTO::getProviderState,
                        InitiateUkDomesticPaymentResponseDTO::getPaymentExecutionContextMetadata
                )
                .contains(
                        "",
                        "",
                        fakePecResult.toMetadata()
                );
    }
}