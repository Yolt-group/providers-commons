package com.yolt.providers.common.domain.authenticationmeans.types;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class NoWhiteCharacterStringTypeTest {

    private static Stream<Arguments> data() {
        return Stream.of(
                Arguments.of("correct-string", true),
                Arguments.of(" ", false),
                Arguments.of("\n", false),
                Arguments.of("\r", false),
                Arguments.of("\r\n", false),
                Arguments.of("\t", false),
                Arguments.of("", false),
                Arguments.of("String with spaces", false),
                Arguments.of("String\twith\ttabulations", false),
                Arguments.of("String\nwith\nLF", false),
                Arguments.of("String\rwith\rCR", false),
                Arguments.of("String\r\nwith\r\nCRLF", false),
                Arguments.of("Special-characters:`~!@#$%^&*()_+={}[]|\\;\"<,>.?/", true)
        );
    }

    @ParameterizedTest
    @MethodSource("data")
    void shouldValidStringForNoWhiteCharacterStringType(String paramToCheck, boolean expected) {
        // given
        NoWhiteCharacterStringType type = NoWhiteCharacterStringType.getInstance();

        // when
        boolean result = type.isStringValid(paramToCheck);

        // then
        assertThat(result).isEqualTo(expected);
    }
}