package com.yolt.providers.common.pis.paymentexecutioncontext.adapter.uk.initiate;

import com.yolt.providers.common.pis.paymentexecutioncontext.CommonPaymentExecutionContext;
import com.yolt.providers.common.pis.paymentexecutioncontext.model.*;
import com.yolt.providers.common.pis.uk.InitiateUkDomesticPaymentRequestBuilder;
import com.yolt.providers.common.pis.ukdomestic.InitiateUkDomesticPaymentRequest;
import com.yolt.providers.common.pis.ukdomestic.InitiateUkDomesticPaymentResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UkInitiateSinglePaymentExecutionContextAdapterTest {

    @InjectMocks
    private UkInitiateSinglePaymentExecutionContextAdapter<String, String, String> ukInitiatePaymentExecutionContextAdapter;

    @Mock
    private CommonPaymentExecutionContext<String, String, String> ukInitiatePaymentExecutionContext;

    @Mock
    private PaymentAuthorizationUrlExtractor<String, String> authorizationUrlExtractor;

    @Mock
    private UkPaymentProviderStateExtractor<String, String> ukPaymentProviderStateExtractor;

    @Mock
    private CommonUkInitiatePaymentExecutionContextAdapter<InitiateUkDomesticPaymentRequest, String, String, String> commonUkInitiatePaymentExecutionContextAdapter;

    @Captor
    private ArgumentCaptor<PaymentPreExecutionResultProvider<String>> paymentPreExecutionResultProviderArgumentCaptor;

    @Test
    void shouldReturnInitiateUkDomesticPaymentResponseDTOWithAuthUrlProviderStateAndPecMetadataForInitiateSinglePaymentWithCorrectData() {
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

        given(commonUkInitiatePaymentExecutionContextAdapter.initiatePayment(any(InitiateUkDomesticPaymentRequest.class)))
                .willReturn(new InitiateUkDomesticPaymentResponseDTO("fakeAuthUrl",
                        "fakeUkProviderState",
                        fakePecResult.toMetadata()));

        // when
        InitiateUkDomesticPaymentResponseDTO result = ukInitiatePaymentExecutionContextAdapter.initiateSinglePayment(fakeInitiateUkDomesticPaymentRequest);

        // then
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
}