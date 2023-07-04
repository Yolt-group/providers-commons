package com.yolt.providers.common.ais.url;

import com.yolt.providers.common.cryptography.RestTemplateManager;
import com.yolt.providers.common.cryptography.Signer;
import lombok.NoArgsConstructor;
import nl.ing.lovebird.providershared.AccessMeansDTO;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@NoArgsConstructor
public class UrlRefreshAccessMeansRequestBuilderTest {

    /**
     * Test asserts that every field of {@link UrlRefreshAccessMeansRequest} class is set via {@link UrlRefreshAccessMeansRequestBuilder}.
     * If it fails, add missing field to a {@link UrlRefreshAccessMeansRequestBuilder}
     */
    @Test
    public void shouldSetAllFieldsViaBuilder() throws IllegalAccessException {
        // given
        UrlRefreshAccessMeansRequestBuilder subject = new UrlRefreshAccessMeansRequestBuilder();

        UrlRefreshAccessMeansRequest builtObject = subject
                .setAccessMeans(mock(AccessMeansDTO.class))
                .setAuthenticationMeans(emptyMap())
                .setSigner(mock(Signer.class))
                .setRestTemplateManager(mock(RestTemplateManager.class))
                .setPsuIpAddress("psuIpAddress")
                .build();

        for (Field field : UrlRefreshAccessMeansRequest.class.getDeclaredFields()) {
                field.setAccessible(true);
                // when
                Object fieldValue = field.get(builtObject);

                // then
                assertThat(fieldValue).as(field.getName()).isNotNull();
        }
    }
}