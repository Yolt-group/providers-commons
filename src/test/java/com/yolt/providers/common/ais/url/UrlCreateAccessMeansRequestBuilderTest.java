package com.yolt.providers.common.ais.url;

import com.yolt.providers.common.cryptography.RestTemplateManager;
import com.yolt.providers.common.cryptography.Signer;
import lombok.NoArgsConstructor;
import nl.ing.lovebird.providershared.form.FilledInUserSiteFormValues;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.UUID;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@NoArgsConstructor
public class UrlCreateAccessMeansRequestBuilderTest {

    /**
     * Test asserts that every field of {@link UrlCreateAccessMeansRequest} class is set via {@link UrlCreateAccessMeansRequestBuilder}.
     * If it fails, add missing field to a {@link UrlCreateAccessMeansRequestBuilder}
     */
    @Test
    public void shouldSetAllFieldsViaBuilder() throws IllegalAccessException {
        // given
        UrlCreateAccessMeansRequestBuilder subject = new UrlCreateAccessMeansRequestBuilder();

        UrlCreateAccessMeansRequest builtObject = subject
                .setUserId(UUID.randomUUID())
                .setBaseClientRedirectUrl("redirectUrl")
                .setRedirectUrlPostedBackFromSite("")
                .setState("")
                .setAuthenticationMeans(emptyMap())
                .setProviderState("providerState")
                .setSigner(mock(Signer.class))
                .setRestTemplateManager(mock(RestTemplateManager.class))
                .setFilledInUserSiteFormValues(mock(FilledInUserSiteFormValues.class))
                .setPsuIpAddress("psuIpAddress")
                .build();

        for (Field field : UrlCreateAccessMeansRequest.class.getDeclaredFields()) {
                field.setAccessible(true);
                // when
                Object fieldValue = field.get(builtObject);

                // then
                assertThat(fieldValue).as(field.getName()).isNotNull();
        }
    }
}