package com.yolt.providers.common.pis.sepa;

import com.yolt.providers.common.cryptography.RestTemplateManager;
import com.yolt.providers.common.cryptography.Signer;
import nl.ing.lovebird.providershared.api.AuthenticationMeansReference;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class InitiatePaymentRequestBuilderTest {

    /**
     * Test asserts that every field of {@link InitiatePaymentRequest} class is set via {@link InitiatePaymentRequestBuilder}.
     * If it fails, add missing field to a {@link InitiatePaymentRequestBuilder}
     */
    @Test
    public void shouldSetAllFieldsViaBuilder() throws IllegalAccessException {
        // given
        InitiatePaymentRequestBuilder subject = new InitiatePaymentRequestBuilder();

        InitiatePaymentRequest builtObject = subject
                .setRequestDTO(mock(SepaInitiatePaymentRequestDTO.class))
                .setBaseClientRedirectUrl("baseRedirectUrl")
                .setState("state")
                .setAuthenticationMeans(emptyMap())
                .setSigner(mock(Signer.class))
                .setRestTemplateManager(mock(RestTemplateManager.class))
                .setPsuIpAddress("psuIpAddress")
                .setAuthenticationMeansReference(mock(AuthenticationMeansReference.class))
                .build();

        for (Field field : InitiatePaymentRequest.class.getDeclaredFields()) {
            field.setAccessible(true);
            // when
            Object fieldValue = field.get(builtObject);

            // then
            assertThat(fieldValue).as(field.getName()).isNotNull();
        }
    }
}
