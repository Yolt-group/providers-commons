package com.yolt.providers.common.ais.url;

import com.yolt.providers.common.cryptography.RestTemplateManager;
import com.yolt.providers.common.cryptography.Signer;
import com.yolt.providers.common.domain.autoonboarding.RegistrationOperation;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Collections;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@NoArgsConstructor
public class UrlAutoOnboardingRequestBuilderTest {

    /**
     * Test asserts that every field of {@link UrlAutoOnboardingRequest} class is set via {@link UrlAutoOnboardingRequestBuilder}.
     * If it fails, add missing field to a {@link UrlAutoOnboardingRequestBuilder}
     */
    @Test
    public void shouldSetAllFields() throws IllegalAccessException {
        // given
        UrlAutoOnboardingRequestBuilder subject = new UrlAutoOnboardingRequestBuilder();

        UrlAutoOnboardingRequest builtObject = subject
                .setBaseClientRedirectUrl("")
                .setRedirectUrls(Collections.singletonList(""))
                .setScopes(Collections.emptySet())
                .setAuthenticationMeans(emptyMap())
                .setSigner(mock(Signer.class))
                .setRestTemplateManager(mock(RestTemplateManager.class))
                .build();

        for (Field field : UrlAutoOnboardingRequest.class.getDeclaredFields()) {
            field.setAccessible(true);
            // when
            Object fieldValue = field.get(builtObject);

            // then
            assertThat(fieldValue).as(field.getName()).isNotNull();
        }
    }

    @Test
    public void shouldSetRegistrationOperation() {
        // given
        UrlAutoOnboardingRequestBuilder subject = new UrlAutoOnboardingRequestBuilder();
        RegistrationOperation updateRegistrationOperation = RegistrationOperation.UPDATE;

        // when
        UrlAutoOnboardingRequest builtObject = subject
                .setRegistrationOperation(updateRegistrationOperation)
                .build();

        // then
        assertThat(builtObject.getRegistrationOperation()).isEqualTo(updateRegistrationOperation);
    }
}