package com.yolt.providers.common.pis.sepa;

import com.yolt.providers.common.cryptography.RestTemplateManager;
import com.yolt.providers.common.cryptography.Signer;
import nl.ing.lovebird.providershared.api.AuthenticationMeansReference;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class GetStatusRequestBuilderTest {

    /**
     * Test asserts that every field of {@link GetStatusRequest} class is set via {@link GetStatusRequestBuilder}.
     * If it fails, add missing field to a {@link GetStatusRequestBuilder}
     */
    @Test
    public void shouldSetAllFieldsViaBuilder() throws IllegalAccessException {
        // given
        GetStatusRequestBuilder subject = new GetStatusRequestBuilder();

        GetStatusRequest builtObject = subject
                .setProviderState("state")
                .setPaymentId("paymentId")
                .setAuthenticationMeans(emptyMap())
                .setSigner(mock(Signer.class))
                .setRestTemplateManager(mock(RestTemplateManager.class))
                .setPsuIpAddress("psuIpAddress")
                .setAuthenticationMeansReference(mock(AuthenticationMeansReference.class))
                .build();

        for (Field field : GetStatusRequest.class.getDeclaredFields()) {
            field.setAccessible(true);
            // when
            Object fieldValue = field.get(builtObject);

            // then
            assertThat(fieldValue).as(field.getName()).isNotNull();
        }
    }
}
