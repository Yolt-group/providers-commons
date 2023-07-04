package com.yolt.providers.common.pis.paymentexecutioncontext.adapter.uk.submit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yolt.providers.common.pis.common.PaymentStatusResponseDTO;
import com.yolt.providers.common.pis.common.PaymentType;
import com.yolt.providers.common.pis.common.SubmitPaymentRequest;
import com.yolt.providers.common.pis.paymentexecutioncontext.CommonPaymentExecutionContext;
import com.yolt.providers.common.pis.paymentexecutioncontext.adapter.uk.initiate.UkPaymentProviderStateExtractor;
import com.yolt.providers.common.pis.paymentexecutioncontext.exception.PaymentExecutionTechnicalException;
import com.yolt.providers.common.pis.paymentexecutioncontext.model.*;
import com.yolt.providers.common.pis.uk.SubmitPaymentRequestBuilder;
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
public class UkSubmitPaymentExecutionContextAdapterTest {

    @InjectMocks
    private UkSubmitPaymentExecutionContextAdapter<String, String, String> ukSubmitPaymentExecutionContextAdapter;

    @Mock
    private CommonPaymentExecutionContext<String, String, String> ukSubmitPaymentExecutionContext;

    @Mock
    private UkDomesticSubmitPreExecutionResultMapper<String> ukDomesticSubmitPreExecutionResultMapper;

    @Mock
    private PaymentIdExtractor<String, String> paymentIdExtractor;

    @Captor
    private ArgumentCaptor<PaymentPreExecutionResultProvider<String>> paymentPreExecutionResultProviderArgumentCaptor;

    @Mock
    private UkPaymentProviderStateExtractor<String, String> ukPaymentProviderStateExtractor;

    @Mock
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnPaymentStatusResponseDTOWithPaymentIdAndPecMetadataForSubmitPaymentWithCorrectData() throws JsonProcessingException {
        // given
        SubmitPaymentRequest fakeSubmitPaymentRequest = new SubmitPaymentRequestBuilder()
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
                "fakePreExecutionResult"
        );
        UkProviderState fakeUkProviderState = new UkProviderState("consentIdValue", PaymentType.SINGLE, "fakeResponseBody");
        given(ukSubmitPaymentExecutionContext.execute(any()))
                .willReturn(fakePecResult);
        given(ukDomesticSubmitPreExecutionResultMapper.map(any(SubmitPaymentRequest.class)))
                .willReturn("fakePreExecutionResult");
        given(paymentIdExtractor.extractPaymentId(anyString(), anyString()))
                .willReturn("fakePaymentId");
        given(ukPaymentProviderStateExtractor.extractUkProviderState(any(), any()))
                .willReturn(fakeUkProviderState);
        given(objectMapper.writeValueAsString(any()))
                .willReturn("{\"consentId\" :\"consentIdValue\", \"paymentType\" : \"SINGLE\", \"openBankingPayment\" : \"fakeResponseBody\"}");

        // when
        PaymentStatusResponseDTO result = ukSubmitPaymentExecutionContextAdapter.submitPayment(fakeSubmitPaymentRequest);

        // then
        then(ukSubmitPaymentExecutionContext)
                .should()
                .execute(paymentPreExecutionResultProviderArgumentCaptor.capture());
        PaymentPreExecutionResultProvider<String> preExecutionResultProvider = paymentPreExecutionResultProviderArgumentCaptor.getValue();
        String preExecutionResult = preExecutionResultProvider.providePreExecutionResult();
        assertThat(preExecutionResult).isEqualTo("fakePreExecutionResult");
        then(ukDomesticSubmitPreExecutionResultMapper)
                .should()
                .map(fakeSubmitPaymentRequest);
        then(paymentIdExtractor)
                .should()
                .extractPaymentId("fakeResponseBody", "fakePreExecutionResult");
        then(ukPaymentProviderStateExtractor)
                .should()
                .extractUkProviderState("fakeResponseBody", "fakePreExecutionResult");
        then(objectMapper)
                .should()
                .writeValueAsString(fakeUkProviderState);
        assertThat(result)
                .extracting(
                        PaymentStatusResponseDTO::getProviderState,
                        PaymentStatusResponseDTO::getPaymentId,
                        PaymentStatusResponseDTO::getPaymentExecutionContextMetadata
                )
                .contains(
                        "{\"consentId\" :\"consentIdValue\", \"paymentType\" : \"SINGLE\", \"openBankingPayment\" : \"fakeResponseBody\"}",
                        "fakePaymentId",
                        fakePecResult.toMetadata()
                );
    }

    @Test
    void shouldReturnPaymentStatusResponseDTOWithEmptyPaymentIdForSubmitPaymentWhenHttpResponseBodyIsNotAvailableInPecResult() {
        // given
        SubmitPaymentRequest fakeSubmitPaymentRequest = new SubmitPaymentRequestBuilder()
                .build();
        PaymentExecutionResult<String, String> fakePecResult = new PaymentExecutionResult<>(
                Instant.now(),
                Instant.now(),
                "",
                "",
                "",
                "",
                new PaymentStatuses(RawBankPaymentStatus.unknown(), EnhancedPaymentStatus.REJECTED),
                null,
                null
        );
        given(ukSubmitPaymentExecutionContext.execute(any()))
                .willReturn(fakePecResult);

        // when
        PaymentStatusResponseDTO result = ukSubmitPaymentExecutionContextAdapter.submitPayment(fakeSubmitPaymentRequest);

        // then
        assertThat(result)
                .extracting(
                        PaymentStatusResponseDTO::getPaymentId,
                        PaymentStatusResponseDTO::getPaymentExecutionContextMetadata
                )
                .contains(
                        "",
                        fakePecResult.toMetadata()
                );
    }

    @Test
    void shouldThrowPaymentExecutionTechnicalExceptionForSubmitPaymentWhenPaymentTechnicalExceptionIsThrownByPaymentIdExtractor() {
        // given
        SubmitPaymentRequest fakeSubmitPaymentRequest = new SubmitPaymentRequestBuilder()
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
                "fakePreExecutionResult"
        );
        given(ukSubmitPaymentExecutionContext.execute(any()))
                .willReturn(fakePecResult);
        given(paymentIdExtractor.extractPaymentId(anyString(), anyString()))
                .willThrow(RuntimeException.class);

        // when
        ThrowableAssert.ThrowingCallable callable = () -> ukSubmitPaymentExecutionContextAdapter.submitPayment(fakeSubmitPaymentRequest);

        // then
        assertThatExceptionOfType(PaymentExecutionTechnicalException.class)
                .isThrownBy(callable)
                .withMessage("submit_preparation_error")
                .withCauseInstanceOf(RuntimeException.class);
    }

    @Test
    void shouldThrowPaymentExecutionTechnicalExceptionForSubmitPaymentWhenPaymentTechnicalExceptionIsThrownByUkPaymentProviderStateExtractor() {
        // given
        SubmitPaymentRequest fakeSubmitPaymentRequest = new SubmitPaymentRequestBuilder()
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
                "fakePreExecutionResult"
        );
        given(ukSubmitPaymentExecutionContext.execute(any()))
                .willReturn(fakePecResult);
        given(ukPaymentProviderStateExtractor.extractUkProviderState(any(), any()))
                .willThrow(NullPointerException.class);

        // when
        ThrowableAssert.ThrowingCallable callable = () -> ukSubmitPaymentExecutionContextAdapter.submitPayment(fakeSubmitPaymentRequest);

        // then
        assertThatExceptionOfType(PaymentExecutionTechnicalException.class)
                .isThrownBy(callable)
                .withMessage("submit_preparation_error")
                .withCauseInstanceOf(NullPointerException.class);
    }
}
