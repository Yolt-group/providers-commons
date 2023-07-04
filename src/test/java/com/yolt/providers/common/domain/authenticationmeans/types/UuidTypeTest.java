package com.yolt.providers.common.domain.authenticationmeans.types;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class UuidTypeTest {

    private static Stream<Arguments> data() {
        return Stream.of(
                Arguments.of("2892c735-9792-445b-a928-9464678e4e9f", true),
                Arguments.of("7268BFA4-1F24-4800-91E0-41DC07E58DFA", true),
                Arguments.of("invalid UUID", false),
                Arguments.of("2892c735-9792-445b-a928-9464678e4e9", false),
                Arguments.of("2892c735-9792-445b-a92-9464678e4e9f", false),
                Arguments.of("2892c735-9792-445-a928-9464678e4e9f", false),
                Arguments.of("2892c735-979-445b-a928-9464678e4e9f", false),
                Arguments.of("2892c73-9792-445b-a928-9464678e4e9f", false),
                Arguments.of("2892c735-9792-445b-a928--9464678e4e9f", false),
                Arguments.of("2892c735-9792-445b-a928-9464+78e4e9f", false),
                Arguments.of("", false),
                Arguments.of(" ", false)
        );
    }

    @ParameterizedTest
    @MethodSource("data")
    void shouldValidStringForUuidType(String paramToCheck, boolean expected) {
        // given
        UuidType type = UuidType.getInstance();

        // when
        boolean result = type.isStringValid(paramToCheck);

        // then
        assertThat(result).isEqualTo(expected);
    }
}