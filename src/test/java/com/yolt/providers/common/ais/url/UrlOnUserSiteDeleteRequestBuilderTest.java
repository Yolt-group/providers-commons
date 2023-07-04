package com.yolt.providers.common.ais.url;

import com.yolt.providers.common.cryptography.RestTemplateManager;
import com.yolt.providers.common.cryptography.Signer;
import lombok.NoArgsConstructor;
import nl.ing.lovebird.providershared.AccessMeansDTO;
import nl.ing.lovebird.providershared.api.AuthenticationMeansReference;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.UUID;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@NoArgsConstructor
public class UrlOnUserSiteDeleteRequestBuilderTest {

    /**
     * Test asserts that every field of {@link UrlOnUserSiteDeleteRequest} class is set via {@link UrlOnUserSiteDeleteRequestBuilder}.
     * If it fails, add missing field to a {@link UrlOnUserSiteDeleteRequestBuilder}
     */
    @Test
    public void shouldSetAllFieldsViaBuilder() throws IllegalAccessException {
        // given
        UrlOnUserSiteDeleteRequestBuilder subject = new UrlOnUserSiteDeleteRequestBuilder();

        UrlOnUserSiteDeleteRequest builtObject = subject
                .setExternalConsentId("externalConsentId")
                .setAuthenticationMeansReference(mock(AuthenticationMeansReference.class))
                .setAuthenticationMeans(emptyMap())
                .setAccessMeans(new AccessMeansDTO(UUID.randomUUID(), "", new Date(), new Date()))
                .setSigner(mock(Signer.class))
                .setRestTemplateManager(mock(RestTemplateManager.class))
                .setPsuIpAddress("psuIpAddress")
                .build();

        for (Field field : UrlOnUserSiteDeleteRequest.class.getDeclaredFields()) {
            field.setAccessible(true);
            // when
            Object fieldValue = field.get(builtObject);

            // then
            assertThat(fieldValue).as(field.getName()).isNotNull();
        }
    }
}