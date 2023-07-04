package com.yolt.providers.common.pis.paymentexecutioncontext.adapter.sepa.initiate;

import com.yolt.providers.common.pis.paymentexecutioncontext.CommonPaymentExecutionContext;
import com.yolt.providers.common.pis.paymentexecutioncontext.exception.PaymentExecutionTechnicalException;
import com.yolt.providers.common.pis.paymentexecutioncontext.model.*;
import com.yolt.providers.common.pis.sepa.InitiatePaymentRequest;
import com.yolt.providers.common.pis.sepa.InitiatePaymentRequestBuilder;
import com.yolt.providers.common.pis.sepa.LoginUrlAndStateDTO;
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
public class SepaInitiatePaymentExecutionContextAdapterTest {

    @InjectMocks
    private SepaInitiatePaymentExecutionContextAdapter<String, String, String> sepaInitiatePaymentExecutionContextAdapter;

    @Mock
    private CommonPaymentExecutionContext<String, String, String> sepaInitiatePaymentExecutionContext;

    @Mock
    private SepaInitiatePaymentPreExecutionResultMapper<String> sepaInitiatePaymentPreExecutionResultMapper;

    @Mock
    private PaymentAuthorizationUrlExtractor<String, String> authorizationUrlExtractor;

    @Mock
    private SepaPaymentProviderStateExtractor<String, String> providerStateExtractor;

    private final Clock clock = Clock.fixed(Instant.parse("2021-01-01T00:00:00Z"), ZoneOffset.UTC);

    @Captor
    private ArgumentCaptor<PaymentPreExecutionResultProvider<String>> paymentPreExecutionResultProviderArgumentCaptor;

    @Test
    void shouldReturnLoginUrlAndStateDTOWithAllDataWhenPaymentExecutionResultWithNonNullResponseBody() {
        // given
        InitiatePaymentRequest fakeInitiatePaymentRequest = new InitiatePaymentRequestBuilder()
                .build();
        PaymentExecutionResult<String, String> fakePecResult = new PaymentExecutionResult<>(
                clock.instant(),
                clock.instant(),
                "fakeRawRequestBody",
                "fakeRawResponseBody",
                "[]",
                "[]",
                new PaymentStatuses(RawBankPaymentStatus.forStatus("bank raw status", "reason"), EnhancedPaymentStatus.INITIATION_SUCCESS),
                "fakeResponseBody",
                "fakePreExecutionResult"
        );
        given(sepaInitiatePaymentExecutionContext.execute(any()))
                .willReturn(fakePecResult);
        given(sepaInitiatePaymentPreExecutionResultMapper.map(any(InitiatePaymentRequest.class)))
                .willReturn("fakePreExecutionResult");
        given(authorizationUrlExtractor.extractAuthorizationUrl(anyString(), anyString()))
                .willReturn("fakeAuthUrl");
        given(providerStateExtractor.extractProviderState(anyString(), anyString()))
                .willReturn("fakeProviderState");

        // when
        LoginUrlAndStateDTO result = sepaInitiatePaymentExecutionContextAdapter.initiatePayment(fakeInitiatePaymentRequest);

        // then
        then(sepaInitiatePaymentExecutionContext)
                .should()
                .execute(paymentPreExecutionResultProviderArgumentCaptor.capture());
        PaymentPreExecutionResultProvider<String> preExecutionResultProvider = paymentPreExecutionResultProviderArgumentCaptor.getValue();
        String preExecutionResult = preExecutionResultProvider.providePreExecutionResult();
        assertThat(preExecutionResult).isEqualTo("fakePreExecutionResult");
        then(sepaInitiatePaymentPreExecutionResultMapper)
                .should()
                .map(fakeInitiatePaymentRequest);
        then(authorizationUrlExtractor)
                .should()
                .extractAuthorizationUrl("fakeResponseBody", "fakePreExecutionResult");
        then(providerStateExtractor)
                .should()
                .extractProviderState("fakeResponseBody", "fakePreExecutionResult");
        assertThat(result).extracting(
                        LoginUrlAndStateDTO::getLoginUrl,
                        LoginUrlAndStateDTO::getProviderState,
                        LoginUrlAndStateDTO::getPaymentExecutionContextMetadata)
                .contains(
                        "fakeAuthUrl",
                        "fakeProviderState",
                        fakePecResult.toMetadata()
                );
    }

    @Test
    void shouldReturnLoginUrlAndSateDTOWithEmptyLoginUrlAndEmptyProviderStateWhenNoResponseBodyInResult() {
        // given
        InitiatePaymentRequest fakeInitiatePaymentRequest = new InitiatePaymentRequestBuilder()
                .build();
        PaymentExecutionResult<String, String> fakePecResult = new PaymentExecutionResult<>(
                clock.instant(),
                clock.instant(),
                "fakeRawRequestBody",
                "fakeRawResponseBody",
                "[]",
                "[]",
                new PaymentStatuses(RawBankPaymentStatus.forStatus("bank raw status", "reason"), EnhancedPaymentStatus.INITIATION_SUCCESS),
                null,
                null
        );
        given(sepaInitiatePaymentExecutionContext.execute(any()))
                .willReturn(fakePecResult);

        // when
        LoginUrlAndStateDTO result = sepaInitiatePaymentExecutionContextAdapter.initiatePayment(fakeInitiatePaymentRequest);

        // then
        assertThat(result).extracting(
                        LoginUrlAndStateDTO::getLoginUrl,
                        LoginUrlAndStateDTO::getProviderState,
                        LoginUrlAndStateDTO::getPaymentExecutionContextMetadata)
                .contains(
                        "",
                        "",
                        fakePecResult.toMetadata()
                );
    }

    @Test
    void shouldThrowPaymentExecutionTechnicalExceptionForInitiatePaymentWhenAnyExceptionIsThrownUponResponseBodyHandling() {
        // given
        InitiatePaymentRequest fakeInitiatePaymentRequest = new InitiatePaymentRequestBuilder()
                .build();
        PaymentExecutionResult<String, String> fakePecResult = new PaymentExecutionResult<>(
                clock.instant(),
                clock.instant(),
                "fakeRawRequestBody",
                "fakeRawResponseBody",
                "[]",
                "[]",
                new PaymentStatuses(RawBankPaymentStatus.forStatus("bank raw status", "reason"), EnhancedPaymentStatus.INITIATION_SUCCESS),
                "fakeResponseBody",
                "fakePreExecutionResult"
        );
        given(sepaInitiatePaymentExecutionContext.execute(any()))
                .willReturn(fakePecResult);
        given(authorizationUrlExtractor.extractAuthorizationUrl(anyString(), anyString()))
                .willThrow(RuntimeException.class);

        // when
        ThrowableAssert.ThrowingCallable callable = () -> sepaInitiatePaymentExecutionContextAdapter.initiatePayment(fakeInitiatePaymentRequest);

        // then
        assertThatExceptionOfType(PaymentExecutionTechnicalException.class)
                .isThrownBy(callable)
                .withMessage("request_creation_error")
                .withCauseInstanceOf(RuntimeException.class);
    }
}
