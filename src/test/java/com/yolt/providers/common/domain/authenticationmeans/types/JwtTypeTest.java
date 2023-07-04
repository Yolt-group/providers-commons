package com.yolt.providers.common.domain.authenticationmeans.types;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtTypeTest {

    private final static String PROPER_JWT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
    private final static String NOT_BASED_JWT = "header.payload.signature";

    private static Stream<Arguments> data() {
        return Stream.of(
                Arguments.of("anyString", false),
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
                Arguments.of(" String prefixed with space", false),
                Arguments.of("\tString prefixed with tabulation", false),
                Arguments.of("\nString prefixed with LF", false),
                Arguments.of("\rString prefixed with CR", false),
                Arguments.of("\r\nString prefixed with CRLF", false),
                Arguments.of("header.payload\t.signature\n", false),
                Arguments.of(NOT_BASED_JWT, false),
                Arguments.of(PROPER_JWT, true)
        );
    }

    @ParameterizedTest
    @MethodSource("data")
    void shouldValidStringForJwtType(String paramToCheck, boolean expected) {
        // given
        JwtType type = JwtType.getInstance();

        // when
        boolean result = type.isStringValid(paramToCheck);

        // then
        assertThat(result).isEqualTo(expected);
    }
}
