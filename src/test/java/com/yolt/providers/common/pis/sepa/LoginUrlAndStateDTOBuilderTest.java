package com.yolt.providers.common.pis.sepa;

import org.junit.jupiter.api.Test;
import org.springframework.lang.Nullable;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginUrlAndStateDTOBuilderTest {

    /**
     * Test asserts that every field of {@link LoginUrlAndStateDTO} class is set via {@link LoginUrlAndStateDTOBuilder}.
     * If it fails, add missing field to a {@link LoginUrlAndStateDTOBuilder}
     */
    @Test
    public void shouldSetAllFieldsViaBuilder() throws IllegalAccessException {
        // given
        LoginUrlAndStateDTOBuilder subject = new LoginUrlAndStateDTOBuilder();

        LoginUrlAndStateDTO builtObject = subject
                .setLoginUrl("loginUrl")
                .setProviderState("providerState")
                .build();

        for (Field field : LoginUrlAndStateDTO.class.getDeclaredFields()) {
            if (field.isAnnotationPresent(Nullable.class)) {
                continue;
            }
            field.setAccessible(true);
            // when
            Object fieldValue = field.get(builtObject);

            // then
            assertThat(fieldValue).as(field.getName()).isNotNull();
        }
    }
}
