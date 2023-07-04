package com.yolt.providers.common.pis.sepa;

import com.yolt.providers.common.cryptography.RestTemplateManager;
import com.yolt.providers.common.cryptography.Signer;
import nl.ing.lovebird.providershared.api.AuthenticationMeansReference;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class SubmitPaymentRequestBuilderTest {

    /**
     * Test asserts that every field of {@link SubmitPaymentRequest} class is set via {@link SubmitPaymentRequestBuilder}.
     * If it fails, add missing field to a {@link SubmitPaymentRequestBuilder}
     */
    @Test
    public void shouldSetAllFieldsViaBuilder() throws IllegalAccessException {
        // given
        SubmitPaymentRequestBuilder subject = new SubmitPaymentRequestBuilder();

        SubmitPaymentRequest builtObject = subject
                .setProviderState("providerState")
                .setAuthenticationMeans(emptyMap())
                .setRedirectUrlPostedBackFromSite("redirectUrl")
                .setSigner(mock(Signer.class))
                .setRestTemplateManager(mock(RestTemplateManager.class))
                .setPsuIpAddress("psuIpAddress")
                .setAuthenticationMeansReference(mock(AuthenticationMeansReference.class))
                .build();

        for (Field field : SubmitPaymentRequest.class.getDeclaredFields()) {
            field.setAccessible(true);
            // when
            Object fieldValue = field.get(builtObject);

            // then
            assertThat(fieldValue).as(field.getName()).isNotNull();
        }
    }
}
