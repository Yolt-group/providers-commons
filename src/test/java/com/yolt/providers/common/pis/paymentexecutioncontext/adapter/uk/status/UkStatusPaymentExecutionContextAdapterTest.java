package com.yolt.providers.common.pis.paymentexecutioncontext.adapter.uk.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yolt.providers.common.pis.common.GetStatusRequest;
import com.yolt.providers.common.pis.common.PaymentStatusResponseDTO;
import com.yolt.providers.common.pis.common.PaymentType;
import com.yolt.providers.common.pis.paymentexecutioncontext.CommonPaymentExecutionContext;
import com.yolt.providers.common.pis.paymentexecutioncontext.adapter.uk.initiate.UkPaymentProviderStateExtractor;
import com.yolt.providers.common.pis.paymentexecutioncontext.exception.PaymentExecutionTechnicalException;
import com.yolt.providers.common.pis.paymentexecutioncontext.model.*;
import com.yolt.providers.common.pis.uk.GetStatusRequestBuilder;
import com.yolt.providers.common.pis.ukdomestic.UkProviderState;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class UkStatusPaymentExecutionContextAdapterTest {

    @InjectMocks
    private UkStatusPaymentExecutionContextAdapter<String, String, String> ukStatusPaymentExecutionContextAdapter;

    @Mock
    private CommonPaymentExecutionContext<String, String, String> ukStatusPaymentExecutionContext;

    @Mock
    private UkDomesticStatusPaymentPreExecutionResultMapper<String> ukDomesticStatusPaymentPreExecutionResultMapper;

    @Mock
    private PaymentIdExtractor<String, String> paymentIdExtractor;

    @Mock
    private UkPaymentProviderStateExtractor<String, String> ukPaymentProviderStateExtractor;

    @Mock
    private ObjectMapper objectMapper;

    private final Clock clock = Clock.fixed(Instant.parse("2021-01-01T00:00:00Z"), ZoneOffset.UTC);

    @Captor
    private ArgumentCaptor<PaymentPreExecutionResultProvider<String>> paymentPreExecutionResultProviderArgumentCaptor;

    @Test
    void shouldReturnPaymentStatusResponseDTOForGetPaymentStatusWhenCorrectData() throws JsonProcessingException {
        // given
        GetStatusRequest request = new GetStatusRequestBuilder()
                .setProviderState("")
                .setPaymentId("")
                .setAuthenticationMeans(Collections.emptyMap())
                .setPsuIpAddress("")
                .build();
        PaymentExecutionResult<String, String> fakePecResult = new PaymentExecutionResult<>(
                clock.instant(),
                clock.instant(),
                "",
                "fakeRawResponseBody",
                "[]",
                "[]",
                new PaymentStatuses(RawBankPaymentStatus.forStatus("bank raw status", "reason"), EnhancedPaymentStatus.INITIATION_SUCCESS),
                "fakeResponseBody",
                "fakePreExecutionResult"
        );
        UkProviderState fakeUkProviderState = new UkProviderState("consentIdValue", PaymentType.SINGLE, "fakeResponseBody");
        given(ukStatusPaymentExecutionContext.execute(any()))
                .willReturn(fakePecResult);
        given(ukDomesticStatusPaymentPreExecutionResultMapper.map(any(GetStatusRequest.class)))
                .willReturn("fakePreExecutionResult");
        given(paymentIdExtractor.extractPaymentId(anyString(), anyString()))
                .willReturn("fakePaymentId");
        given(ukPaymentProviderStateExtractor.extractUkProviderState(any(), any()))
                .willReturn(fakeUkProviderState);
        given(objectMapper.writeValueAsString(any()))
                .willReturn("{\"consentId\" :\"consentIdValue\", \"paymentType\" : \"SINGLE\", \"openBankingPayment\" : \"fakeResponseBody\"}");

        // when
        PaymentStatusResponseDTO result = ukStatusPaymentExecutionContextAdapter.getPaymentStatus(request);

        // then
        then(ukStatusPaymentExecutionContext)
                .should()
                .execute(paymentPreExecutionResultProviderArgumentCaptor.capture());
        PaymentPreExecutionResultProvider<String> preExecutionResultProvider = paymentPreExecutionResultProviderArgumentCaptor.getValue();
        String preExecutionResult = preExecutionResultProvider.providePreExecutionResult();
        assertThat(preExecutionResult).isEqualTo("fakePreExecutionResult");
        then(ukDomesticStatusPaymentPreExecutionResultMapper)
                .should()
                .map(request);
        then(paymentIdExtractor)
                .should()
                .extractPaymentId("fakeResponseBody", "fakePreExecutionResult");
        then(ukPaymentProviderStateExtractor)
                .should()
                .extractUkProviderState("fakeResponseBody", "fakePreExecutionResult");
        then(objectMapper)
                .should()
                .writeValueAsString(fakeUkProviderState);
        assertThat(result).extracting(
                        PaymentStatusResponseDTO::getProviderState,
                        PaymentStatusResponseDTO::getPaymentId,
                        PaymentStatusResponseDTO::getPaymentExecutionContextMetadata)
                .contains(
                        "{\"consentId\" :\"consentIdValue\", \"paymentType\" : \"SINGLE\", \"openBankingPayment\" : \"fakeResponseBody\"}",
                        "fakePaymentId",
                        fakePecResult.toMetadata());
    }

    @Test
    void shouldReturnPecResultWithEmptyPaymentIdAndProperStatusForGetPaymentStatusWhenNoHttpResponseBodyInPecResult() {
        // given
        GetStatusRequest request = new GetStatusRequestBuilder()
                .setProviderState("providerStateFromRequest")
                .setPaymentId("")
                .setAuthenticationMeans(Collections.emptyMap())
                .setPsuIpAddress("")
                .build();
        PaymentExecutionResult<String, String> fakePecResult = new PaymentExecutionResult<>(
                clock.instant(),
                clock.instant(),
                "",
                "fakeRawResponseBody",
                "[]",
                "[]",
                new PaymentStatuses(RawBankPaymentStatus.forStatus("bank raw status", "reason"), EnhancedPaymentStatus.INITIATION_SUCCESS),
                null,
                null
        );
        given(ukStatusPaymentExecutionContext.execute(any()))
                .willReturn(fakePecResult);

        // when
        PaymentStatusResponseDTO result = ukStatusPaymentExecutionContextAdapter.getPaymentStatus(request);

        // then
        assertThat(result.getPaymentId()).isEmpty();
        assertThat(result.getProviderState()).isEqualTo("providerStateFromRequest");
        assertThat(result.getPaymentExecutionContextMetadata()).satisfies(pecMetadata ->
                assertThat(pecMetadata.getPaymentStatuses()).satisfies(statuses -> {
                    assertThat(statuses.getRawBankPaymentStatus().getStatus()).isEqualTo("bank raw status");
                    assertThat(statuses.getRawBankPaymentStatus().getReason()).isEqualTo("reason");
                    assertThat(statuses.getPaymentStatus()).isEqualTo(EnhancedPaymentStatus.INITIATION_SUCCESS);
                }));
    }

    @Test
    void shouldThrowPaymentExecutionTechnicalExceptionForGetPaymentStatusWhenAnyExceptionIsThrownByIdExtractor() {
        // given
        GetStatusRequest request = new GetStatusRequestBuilder()
                .setProviderState("")
                .setPaymentId("")
                .setAuthenticationMeans(Collections.emptyMap())
                .setPsuIpAddress("")
                .build();
        PaymentExecutionResult<String, String> fakePecResult = new PaymentExecutionResult<>(
                clock.instant(),
                clock.instant(),
                "",
                "fakeRawResponseBody",
                "[]",
                "[]",
                new PaymentStatuses(RawBankPaymentStatus.forStatus("bank raw status", "reason"), EnhancedPaymentStatus.INITIATION_SUCCESS),
                "fakeResponseBody",
                "fakePreExecutionResult"
        );
        given(ukStatusPaymentExecutionContext.execute(any()))
                .willReturn(fakePecResult);
        given(paymentIdExtractor.extractPaymentId(anyString(), anyString()))
                .willThrow(RuntimeException.class);

        // when
        ThrowableAssert.ThrowingCallable callable = () -> ukStatusPaymentExecutionContextAdapter.getPaymentStatus(request);

        // then
        assertThatExceptionOfType(PaymentExecutionTechnicalException.class)
                .isThrownBy(callable)
                .withMessage("status_failed")
                .withCauseInstanceOf(RuntimeException.class);
    }

    @Test
    void shouldThrowPaymentExecutionTechnicalExceptionForGetPaymentStatusWhenAnyExceptionIsThrownByUkProviderStateExtractor() {
        // given
        GetStatusRequest request = new GetStatusRequestBuilder()
                .setProviderState("")
                .setPaymentId("")
                .setAuthenticationMeans(Collections.emptyMap())
                .setPsuIpAddress("")
                .build();
        PaymentExecutionResult<String, String> fakePecResult = new PaymentExecutionResult<>(
                clock.instant(),
                clock.instant(),
                "",
                "fakeRawResponseBody",
                "[]",
                "[]",
                new PaymentStatuses(RawBankPaymentStatus.forStatus("bank raw status", "reason"), EnhancedPaymentStatus.INITIATION_SUCCESS),
                "fakeResponseBody",
                "fakePreExecutionResult"
        );
        given(ukStatusPaymentExecutionContext.execute(any()))
                .willReturn(fakePecResult);
        given(ukPaymentProviderStateExtractor.extractUkProviderState(any(), any()))
                .willThrow(NullPointerException.class);

        // when
        ThrowableAssert.ThrowingCallable callable = () -> ukStatusPaymentExecutionContextAdapter.getPaymentStatus(request);

        // then
        assertThatExceptionOfType(PaymentExecutionTechnicalException.class)
                .isThrownBy(callable)
                .withMessage("status_failed")
                .withCauseInstanceOf(NullPointerException.class);
    }
}
