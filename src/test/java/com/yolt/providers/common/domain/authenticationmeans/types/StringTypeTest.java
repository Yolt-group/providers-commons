package com.yolt.providers.common.domain.authenticationmeans.types;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class StringTypeTest {

    private static Stream<Arguments> data() {
        return Stream.of(
                Arguments.of("anyString", true),
                Arguments.of(" ", false),
                Arguments.of("\n", false),
                Arguments.of("\r", false),
                Arguments.of("\r\n", false),
                Arguments.of("\t", false),
                Arguments.of("", false),
                Arguments.of("String with spaces", true),
                Arguments.of("String\twith\ttabulations", true),
                Arguments.of("String\nwith\nLF", true),
                Arguments.of("String\rwith\rCR", true),
                Arguments.of("String\r\nwith\r\nCRLF", true),
                Arguments.of(" String prefixed with space", true),
                Arguments.of("\tString prefixed with tabulation", true),
                Arguments.of("\nString prefixed with LF", true),
                Arguments.of("\rString prefixed with CR", true),
                Arguments.of("\r\nString prefixed with CRLF", true)
        );
    }

    @ParameterizedTest
    @MethodSource("data")
    void shouldValidStringForStringType(String paramToCheck, boolean expected) {
        // given
        StringType type = StringType.getInstance();

        // when
        boolean result = type.isStringValid(paramToCheck);

        // then
        assertThat(result).isEqualTo(expected);
    }
}