package com.yolt.providers.common.pis.paymentexecutioncontext.adapter.sepa.status;

import com.yolt.providers.common.pis.paymentexecutioncontext.CommonPaymentExecutionContext;
import com.yolt.providers.common.pis.paymentexecutioncontext.adapter.sepa.initiate.SepaPaymentProviderStateExtractor;
import com.yolt.providers.common.pis.paymentexecutioncontext.exception.PaymentExecutionTechnicalException;
import com.yolt.providers.common.pis.paymentexecutioncontext.model.*;
import com.yolt.providers.common.pis.sepa.GetStatusRequest;
import com.yolt.providers.common.pis.sepa.GetStatusRequestBuilder;
import com.yolt.providers.common.pis.sepa.SepaPaymentStatusResponseDTO;
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
public class SepaStatusPaymentExecutionContextAdapterTest {

    @InjectMocks
    private SepaStatusPaymentExecutionContextAdapter<String, String, String> sepaStatusPaymentExecutionContextAdapter;

    @Mock
    private CommonPaymentExecutionContext<String, String, String> sepaStatusPaymentExecutionContext;

    @Mock
    private SepaStatusPaymentPreExecutionResultMapper<String> sepaStatusPaymentPreExecutionResultMapper;

    @Mock
    private PaymentIdExtractor<String, String> paymentIdExtractor;

    @Mock
    private SepaPaymentProviderStateExtractor<String, String> providerStateExtractor;

    private final Clock clock = Clock.fixed(Instant.parse("2021-01-01T00:00:00Z"), ZoneOffset.UTC);

    @Captor
    private ArgumentCaptor<PaymentPreExecutionResultProvider<String>> paymentPreExecutionResultProviderArgumentCaptor;

    @Test
    void shouldReturnSepaPaymentStatusResponseDTOWithAllFieldsForGetPaymentStatusWhenCorrectData() {
        // given
        GetStatusRequest getStatusRequest = new GetStatusRequestBuilder()
                .setAuthenticationMeans(Collections.emptyMap())
                .setPaymentId("")
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
        given(sepaStatusPaymentExecutionContext.execute(any()))
                .willReturn(fakePecResult);
        given(sepaStatusPaymentPreExecutionResultMapper.map(any(GetStatusRequest.class)))
                .willReturn("fakePreExecutionResult");
        given(paymentIdExtractor.extractPaymentId(anyString(), anyString()))
                .willReturn("fakePaymentId");
        given(providerStateExtractor.extractProviderState(anyString(), anyString()))
                .willReturn("fakeState");

        // when
        SepaPaymentStatusResponseDTO result = sepaStatusPaymentExecutionContextAdapter.getPaymentStatus(getStatusRequest);

        // then
        then(sepaStatusPaymentExecutionContext)
                .should()
                .execute(paymentPreExecutionResultProviderArgumentCaptor.capture());
        PaymentPreExecutionResultProvider<String> preExecutionResultProvider = paymentPreExecutionResultProviderArgumentCaptor.getValue();
        String preExecutionResult = preExecutionResultProvider.providePreExecutionResult();
        assertThat(preExecutionResult).isEqualTo("fakePreExecutionResult");
        then(sepaStatusPaymentPreExecutionResultMapper)
                .should()
                .map(getStatusRequest);
        then(paymentIdExtractor)
                .should()
                .extractPaymentId("fakeResponseBody", "fakePreExecutionResult");
        then(providerStateExtractor)
                .should()
                .extractProviderState("fakeResponseBody", "fakePreExecutionResult");
        assertThat(result).extracting(
                        SepaPaymentStatusResponseDTO::getProviderState,
                        SepaPaymentStatusResponseDTO::getPaymentId,
                        SepaPaymentStatusResponseDTO::getPaymentExecutionContextMetadata)
                .contains(
                        "fakeState",
                        "fakePaymentId",
                        fakePecResult.toMetadata());
    }

    @Test
    void shouldReturnPecResultWithEmptyPaymentIdAndProperStatusForGetPaymentStatusWhenNoHttpResponseBodyInPecResult() {
        // given
        GetStatusRequest getStatusRequest = new GetStatusRequestBuilder()
                .setAuthenticationMeans(Collections.emptyMap())
                .setPaymentId("")
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
        given(sepaStatusPaymentExecutionContext.execute(any()))
                .willReturn(fakePecResult);

        // when
        SepaPaymentStatusResponseDTO result = sepaStatusPaymentExecutionContextAdapter.getPaymentStatus(getStatusRequest);

        // then
        assertThat(result.getPaymentId()).isEmpty();
        assertThat(result.getPaymentExecutionContextMetadata()).satisfies(pecMetadata ->
                assertThat(pecMetadata.getPaymentStatuses()).satisfies(statuses -> {
                    assertThat(statuses.getRawBankPaymentStatus().getStatus()).isEqualTo("bank raw status");
                    assertThat(statuses.getRawBankPaymentStatus().getReason()).isEqualTo("reason");
                    assertThat(statuses.getPaymentStatus()).isEqualTo(EnhancedPaymentStatus.INITIATION_SUCCESS);
                }));
    }

    @Test
    void shouldThrowPaymentExecutionTechnicalExceptionForGetPaymentStatusWhenAnyExceptionIsThrownByPaymentIdExtractor() {
        // given
        GetStatusRequest getStatusRequest = new GetStatusRequestBuilder()
                .setAuthenticationMeans(Collections.emptyMap())
                .setPaymentId("")
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
        given(sepaStatusPaymentExecutionContext.execute(any()))
                .willReturn(fakePecResult);
        given(paymentIdExtractor.extractPaymentId(anyString(), anyString()))
                .willThrow(RuntimeException.class);

        // when
        ThrowableAssert.ThrowingCallable callable = () -> sepaStatusPaymentExecutionContextAdapter.getPaymentStatus(getStatusRequest);

        // then
        assertThatExceptionOfType(PaymentExecutionTechnicalException.class)
                .isThrownBy(callable)
                .withMessage("status_failed")
                .withCauseInstanceOf(RuntimeException.class);
    }

    @Test
    void shouldThrowPaymentExecutionTechnicalExceptionForGetPaymentStatusWhenAnyExceptionIsThrownByProviderStateExtractor() {
        // given
        GetStatusRequest getStatusRequest = new GetStatusRequestBuilder()
                .setAuthenticationMeans(Collections.emptyMap())
                .setPaymentId("")
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
        given(sepaStatusPaymentExecutionContext.execute(any()))
                .willReturn(fakePecResult);
        given(providerStateExtractor.extractProviderState(anyString(), anyString()))
                .willThrow(RuntimeException.class);

        // when
        ThrowableAssert.ThrowingCallable callable = () -> sepaStatusPaymentExecutionContextAdapter.getPaymentStatus(getStatusRequest);

        // then
        assertThatExceptionOfType(PaymentExecutionTechnicalException.class)
                .isThrownBy(callable)
                .withMessage("status_failed")
                .withCauseInstanceOf(RuntimeException.class);
    }
}
