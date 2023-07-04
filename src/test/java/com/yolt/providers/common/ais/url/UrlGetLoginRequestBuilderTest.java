package com.yolt.providers.common.ais.url;

import com.yolt.providers.common.cryptography.RestTemplateManager;
import com.yolt.providers.common.cryptography.Signer;
import lombok.NoArgsConstructor;
import nl.ing.lovebird.providershared.api.AuthenticationMeansReference;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@NoArgsConstructor
public class UrlGetLoginRequestBuilderTest {

    /**
     * Test asserts that every field of {@link UrlGetLoginRequest} class is set via {@link UrlGetLoginRequestBuilder}.
     * If it fails, add missing field to a {@link UrlGetLoginRequestBuilder}
     */
    @Test
    public void shouldSetAllFieldsViaBuilder() throws IllegalAccessException {
        // given
        UrlGetLoginRequestBuilder subject = new UrlGetLoginRequestBuilder();

        UrlGetLoginRequest builtObject = subject
                .setAuthenticationMeansReference(mock(AuthenticationMeansReference.class))
                .setBaseClientRedirectUrl("")
                .setState("")
                .setAuthenticationMeans(emptyMap())
                .setExternalConsentId("externalConsentId")
                .setSigner(mock(Signer.class))
                .setRestTemplateManager(mock(RestTemplateManager.class))
                .setPsuIpAddress("psuIpAddress")
                .build();

        for (Field field : UrlGetLoginRequest.class.getDeclaredFields()) {
                field.setAccessible(true);
                // when
                Object fieldValue = field.get(builtObject);

                // then
                assertThat(fieldValue).as(field.getName()).isNotNull();
        }
    }
}