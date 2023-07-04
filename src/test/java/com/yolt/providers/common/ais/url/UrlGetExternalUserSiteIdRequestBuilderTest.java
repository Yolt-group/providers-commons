package com.yolt.providers.common.ais.url;

import com.yolt.providers.common.cryptography.RestTemplateManager;
import com.yolt.providers.common.cryptography.Signer;
import nl.ing.lovebird.providershared.AccessMeansDTO;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class UrlGetExternalUserSiteIdRequestBuilderTest {

    /**
     * Test asserts that every field of {@link UrlGetExternalUserSiteIdRequest} class is set via {@link UrlGetExternalUserSiteIdRequestBuilder}.
     * If it fails, add missing field to a {@link UrlGetExternalUserSiteIdRequestBuilder}
     */
    @Test
    public void shouldSetAllFieldsViaBuilder() throws IllegalAccessException {
        // given
        UrlGetExternalUserSiteIdRequestBuilder subject = new UrlGetExternalUserSiteIdRequestBuilder();

        UrlGetExternalUserSiteIdRequest builtObject = subject
                .setAccessMeans(mock(AccessMeansDTO.class))
                .setAuthenticationMeans(emptyMap())
                .setSigner(mock(Signer.class))
                .setRestTemplateManager(mock(RestTemplateManager.class))
                .setPsuIpAddress("psuIpAddress")
                .build();

        for (Field field : UrlGetExternalUserSiteIdRequest.class.getDeclaredFields()) {
                field.setAccessible(true);
                // when
                Object fieldValue = field.get(builtObject);

                // then
                assertThat(fieldValue).as(field.getName()).isNotNull();
        }
    }
}