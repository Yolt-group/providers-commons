package com.yolt.providers.common.pis.paymentexecutioncontext.adapter.sepa.submit;

import com.yolt.providers.common.pis.paymentexecutioncontext.CommonPaymentExecutionContext;
import com.yolt.providers.common.pis.paymentexecutioncontext.adapter.sepa.initiate.SepaPaymentProviderStateExtractor;
import com.yolt.providers.common.pis.paymentexecutioncontext.exception.PaymentExecutionTechnicalException;
import com.yolt.providers.common.pis.paymentexecutioncontext.model.*;
import com.yolt.providers.common.pis.sepa.SepaPaymentStatusResponseDTO;
import com.yolt.providers.common.pis.sepa.SubmitPaymentRequest;
import com.yolt.providers.common.pis.sepa.SubmitPaymentRequestBuilder;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class SepaSubmitPaymentExecutionContextAdapterTest {

    @InjectMocks
    private SepaSubmitPaymentExecutionContextAdapter<String, String, String> sepaSubmitPaymentExecutionContextAdapter;

    @Mock
    private CommonPaymentExecutionContext<String, String, String> sepaSubmitPaymentExecutionContext;

    @Mock
    private SepaSubmitPaymentPreExecutionResultMapper<String> sepaSubmitPaymentPreExecutionResultMapper;

    @Mock
    private PaymentIdExtractor<String, String> paymentIdExtractor;

    @Mock
    private SepaPaymentProviderStateExtractor<String, String> providerStateExtractor;

    private final Clock clock = Clock.fixed(Instant.parse("2021-01-01T00:00:00Z"), ZoneOffset.UTC);

    @Captor
    private ArgumentCaptor<PaymentPreExecutionResultProvider<String>> paymentPreExecutionResultProviderArgumentCaptor;

    @Test
    void shouldReturnSepaPaymentStatusResponseDTOWithAllDataWhenPaymentExecutionResultWithNonNullResponseBody() {
        // given
        SubmitPaymentRequest fakeSubmitPaymentRequest = new SubmitPaymentRequestBuilder()
                .build();
        PaymentExecutionResult<String, String> fakePecResult = new PaymentExecutionResult<>(
                clock.instant(),
                clock.instant(),
                "fakeRawRequestBody",
                "fakeRawResponseBody",
                "[]",
                "[]",
                new PaymentStatuses(RawBankPaymentStatus.forStatus("bank raw status", "reason"), EnhancedPaymentStatus.REJECTED),
                "fakeResponseBody",
                "fakePreExecutionResult"
        );
        given(sepaSubmitPaymentExecutionContext.execute(any()))
                .willReturn(fakePecResult);
        given(sepaSubmitPaymentPreExecutionResultMapper.map(any(SubmitPaymentRequest.class)))
                .willReturn("fakePreExecutionResult");
        given(paymentIdExtractor.extractPaymentId(anyString(), anyString()))
                .willReturn("fakePaymentId");
        given(providerStateExtractor.extractProviderState(anyString(), anyString()))
                .willReturn("fakeState");

        // when
        SepaPaymentStatusResponseDTO result = sepaSubmitPaymentExecutionContextAdapter.submitPayment(fakeSubmitPaymentRequest);

        // then
        then(sepaSubmitPaymentExecutionContext)
                .should()
                .execute(paymentPreExecutionResultProviderArgumentCaptor.capture());
        PaymentPreExecutionResultProvider<String> paymentPreExecutionResultProvider = paymentPreExecutionResultProviderArgumentCaptor.getValue();
        String preExecutionResult = paymentPreExecutionResultProvider.providePreExecutionResult();
        assertThat(preExecutionResult).isEqualTo("fakePreExecutionResult");
        then(sepaSubmitPaymentPreExecutionResultMapper)
                .should()
                .map(fakeSubmitPaymentRequest);
        then(paymentIdExtractor)
                .should()
                .extractPaymentId("fakeResponseBody", "fakePreExecutionResult");
        then(providerStateExtractor)
                .should()
                .extractProviderState("fakeResponseBody", "fakePreExecutionResult");
        assertThat(result)
                .extracting(
                        SepaPaymentStatusResponseDTO::getProviderState,
                        SepaPaymentStatusResponseDTO::getPaymentId,
                        SepaPaymentStatusResponseDTO::getPaymentExecutionContextMetadata
                )
                .contains(
                        "fakeState",
                        "fakePaymentId",
                        fakePecResult.toMetadata()
                );
    }

    @Test
    void shouldReturnSepaPaymentStatusDTOWithEmptyPaymentIdWhenNoResponseBodyInResult() {
        // given
        SubmitPaymentRequest fakeSubmitPaymentRequest = new SubmitPaymentRequestBuilder()
                .build();
        PaymentExecutionResult<String, String> fakePecResult = new PaymentExecutionResult<>(
                clock.instant(),
                clock.instant(),
                "fakeRawRequestBody",
                "fakeRawResponseBody",
                "[]",
                "[]",
                new PaymentStatuses(RawBankPaymentStatus.forStatus("bank raw status", "reason"), EnhancedPaymentStatus.REJECTED),
                null,
                null
        );
        given(sepaSubmitPaymentExecutionContext.execute(any()))
                .willReturn(fakePecResult);

        // when
        SepaPaymentStatusResponseDTO result = sepaSubmitPaymentExecutionContextAdapter.submitPayment(fakeSubmitPaymentRequest);

        // then
        assertThat(result)
                .extracting(
                        SepaPaymentStatusResponseDTO::getPaymentId,
                        SepaPaymentStatusResponseDTO::getPaymentExecutionContextMetadata
                )
                .contains(
                        "",
                        fakePecResult.toMetadata()
                );
    }

    @Test
    void shouldThrowPaymentExecutionTechnicalExceptionForSubmitPaymentWhenAnyExceptionIsThrownByPaymentIdExtactor() {
        // given
        SubmitPaymentRequest fakeSubmitPaymentRequest = new SubmitPaymentRequestBuilder()
                .build();
        PaymentExecutionResult<String, String> fakePecResult = new PaymentExecutionResult<>(
                clock.instant(),
                clock.instant(),
                "fakeRawRequestBody",
                "fakeRawResponseBody",
                "[]",
                "[]",
                new PaymentStatuses(RawBankPaymentStatus.forStatus("bank raw status", "reason"), EnhancedPaymentStatus.REJECTED),
                "fakeResponseBody",
                "fakePreExecutionResult"
        );
        given(sepaSubmitPaymentExecutionContext.execute(any()))
                .willReturn(fakePecResult);
        given(paymentIdExtractor.extractPaymentId(anyString(), anyString()))
                .willThrow(RuntimeException.class);

        // when
        ThrowableAssert.ThrowingCallable callable = () -> sepaSubmitPaymentExecutionContextAdapter.submitPayment(fakeSubmitPaymentRequest);

        // then
        assertThatExceptionOfType(PaymentExecutionTechnicalException.class)
                .isThrownBy(callable)
                .withMessage("submit_preparation_error")
                .withCauseInstanceOf(RuntimeException.class);
    }

    @Test
    void shouldThrowPaymentExecutionTechnicalExceptionForSubmitPaymentWhenAnyExceptionIsThrownByProviderStateExtactor() {
        // given
        SubmitPaymentRequest fakeSubmitPaymentRequest = new SubmitPaymentRequestBuilder()
                .build();
        PaymentExecutionResult<String, String> fakePecResult = new PaymentExecutionResult<>(
                clock.instant(),
                clock.instant(),
                "fakeRawRequestBody",
                "fakeRawResponseBody",
                "[]",
                "[]",
                new PaymentStatuses(RawBankPaymentStatus.forStatus("bank raw status", "reason"), EnhancedPaymentStatus.REJECTED),
                "fakeResponseBody",
                "fakePreExecutionResult"
        );
        given(sepaSubmitPaymentExecutionContext.execute(any()))
                .willReturn(fakePecResult);
        given(providerStateExtractor.extractProviderState(anyString(), anyString()))
                .willThrow(RuntimeException.class);

        // when
        ThrowableAssert.ThrowingCallable callable = () -> sepaSubmitPaymentExecutionContextAdapter.submitPayment(fakeSubmitPaymentRequest);

        // then
        assertThatExceptionOfType(PaymentExecutionTechnicalException.class)
                .isThrownBy(callable)
                .withMessage("submit_preparation_error")
                .withCauseInstanceOf(RuntimeException.class);
    }
}
