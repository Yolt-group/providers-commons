package com.yolt.providers.common.ais.url;

import com.yolt.providers.common.cryptography.RestTemplateManager;
import com.yolt.providers.common.cryptography.Signer;
import lombok.NoArgsConstructor;
import nl.ing.lovebird.providershared.AccessMeansDTO;
import nl.ing.lovebird.providershared.api.AuthenticationMeansReference;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.UUID;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@NoArgsConstructor
public class UrlFetchDataRequestBuilderTest {

    /**
     * Test asserts that every field of {@link UrlFetchDataRequest} class is set via {@link UrlFetchDataRequestBuilder}.
     * If it fails, add missing field to a {@link UrlFetchDataRequestBuilder}
     */
    @Test
    public void shouldSetAllFieldsViaBuilder() throws IllegalAccessException {
        // given
        UrlFetchDataRequestBuilder subject = new UrlFetchDataRequestBuilder();

        UrlFetchDataRequest builtObject = subject
                .setUserId(UUID.randomUUID())
                .setUserSiteId(UUID.randomUUID())
                .setTransactionsFetchStartTime(Instant.now())
                .setAccessMeans(mock(AccessMeansDTO.class))
                .setAuthenticationMeans(emptyMap())
                .setSigner(mock(Signer.class))
                .setRestTemplateManager(mock(RestTemplateManager.class))
                .setAuthenticationMeansReference(mock(AuthenticationMeansReference.class))
                .setPsuIpAddress("psuIpAddress")
                .build();

        for (Field field : UrlFetchDataRequest.class.getDeclaredFields()) {
                field.setAccessible(true);
                // when
                Object fieldValue = field.get(builtObject);

                // then
                assertThat(fieldValue).as(field.getName()).isNotNull();
        }
    }
}