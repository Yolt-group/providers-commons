package com.yolt.providers.common.domain.authenticationmeans.types;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.Assertions.assertThat;

public class PemTypeTest {

    private static final String RSA_PRIVATE_KEY;
    private static final String CERTIFICATE_WITHOUT_NEW_LINES;
    private static final String CERTIFICATE_WITH_NEW_LINES_CRLF;
    private static final String CERTIFICATE_WITH_NEW_LINES_CR;
    private static final String CERTIFICATE_WITH_NEW_LINES_LF;
    private static final String PEM_WITHOUT_BEGIN_AND_END;
    private static final String PEM_WITH_INVALID_CHARACTER;
    private static final String PEM_WITH_WRONG_NUMBER_OF_DASHES_BEFORE_BEGIN;
    private static final String PEM_WITH_WRONG_NUMBER_OF_DASHES_AFTER_BEGIN;
    private static final String PEM_WITH_WRONG_NUMBER_OF_DASHES_BEFORE_END;
    private static final String PEM_WITH_WRONG_NUMBER_OF_DASHES_AFTER_END;
    private static final String PEM_BIGGER_THAN_4096_BYTES;
    private static final String PEM_WITH_THREE_EQUAL_CHARACTERS_AT_THE_END;
    private static final String CERTIFICATE_WITH_NEW_LINES_AFTER_CERTIFICATE;
    private static final String CERTIFICATE_WITH_NEW_LINES_BEFORE_CERTIFICATE;

    static {
        try {
            RSA_PRIVATE_KEY = readFile("RSA_PRIVATE_KEY");
            CERTIFICATE_WITHOUT_NEW_LINES = readFile("CERTIFICATE_WITHOUT_NEW_LINES");
            CERTIFICATE_WITH_NEW_LINES_CRLF = readFile("CERTIFICATE_WITH_NEW_LINES_CRLF");
            CERTIFICATE_WITH_NEW_LINES_CR = readFile("CERTIFICATE_WITH_NEW_LINES_CR");
            CERTIFICATE_WITH_NEW_LINES_LF = readFile("CERTIFICATE_WITH_NEW_LINES_LF");
            PEM_WITHOUT_BEGIN_AND_END = readFile("PEM_WITHOUT_BEGIN_AND_END");
            PEM_WITH_INVALID_CHARACTER = readFile("PEM_WITH_INVALID_CHARACTER");
            PEM_WITH_WRONG_NUMBER_OF_DASHES_BEFORE_BEGIN = readFile("PEM_WITH_WRONG_NUMBER_OF_DASHES_BEFORE_BEGIN");
            PEM_WITH_WRONG_NUMBER_OF_DASHES_AFTER_BEGIN = readFile("PEM_WITH_WRONG_NUMBER_OF_DASHES_AFTER_BEGIN");
            PEM_WITH_WRONG_NUMBER_OF_DASHES_BEFORE_END = readFile("PEM_WITH_WRONG_NUMBER_OF_DASHES_BEFORE_END");
            PEM_WITH_WRONG_NUMBER_OF_DASHES_AFTER_END = readFile("PEM_WITH_WRONG_NUMBER_OF_DASHES_AFTER_END");
            PEM_BIGGER_THAN_4096_BYTES = readFile("PEM_BIGGER_THAN_4096_BYTES");
            PEM_WITH_THREE_EQUAL_CHARACTERS_AT_THE_END = readFile("PEM_WITH_THREE_EQUAL_CHARACTERS_AT_THE_END");
            CERTIFICATE_WITH_NEW_LINES_AFTER_CERTIFICATE = readFile("CERTIFICATE_WITH_NEW_LINES_AFTER_CERTIFICATE");
            CERTIFICATE_WITH_NEW_LINES_BEFORE_CERTIFICATE = readFile("CERTIFICATE_WITH_NEW_LINES_BEFORE_CERTIFICATE");
        } catch (Exception e) {
            fail("could not read json file.");
            throw new RuntimeException("just a runtime exception for the compiler.. We fail anyways.", e);
        }
    }

    private static Stream<Arguments> data() {
        return Stream.of(
                Arguments.of(RSA_PRIVATE_KEY, true),
                Arguments.of(CERTIFICATE_WITHOUT_NEW_LINES, true),
                Arguments.of(CERTIFICATE_WITH_NEW_LINES_CRLF, true),
                Arguments.of(CERTIFICATE_WITH_NEW_LINES_LF, true),
                Arguments.of(CERTIFICATE_WITH_NEW_LINES_CR, true),
                Arguments.of(PEM_WITHOUT_BEGIN_AND_END, false),
                Arguments.of(PEM_WITH_INVALID_CHARACTER, false),
                Arguments.of(PEM_WITH_WRONG_NUMBER_OF_DASHES_AFTER_BEGIN, false),
                Arguments.of(PEM_WITH_WRONG_NUMBER_OF_DASHES_AFTER_END, false),
                Arguments.of(PEM_WITH_WRONG_NUMBER_OF_DASHES_BEFORE_BEGIN, false),
                Arguments.of(PEM_WITH_WRONG_NUMBER_OF_DASHES_BEFORE_END, false),
                Arguments.of(PEM_BIGGER_THAN_4096_BYTES, true),
                Arguments.of(PEM_WITH_THREE_EQUAL_CHARACTERS_AT_THE_END, false),
                Arguments.of(CERTIFICATE_WITH_NEW_LINES_AFTER_CERTIFICATE, true),
                Arguments.of(CERTIFICATE_WITH_NEW_LINES_BEFORE_CERTIFICATE, true),
                Arguments.of("invalid value", false),
                Arguments.of("", false),
                Arguments.of(" ", false)
        );
    }

    @ParameterizedTest
    @MethodSource("data")
    void shouldValidStringForPemType(String paramToCheck, boolean expected) {
        // given
        PemType type = PemType.getInstance();

        // when
        boolean result = type.isStringValid(paramToCheck);

        // then
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("data")
    void shouldPatternMatchForPemType(String paramToCheck, boolean expected) {
        // given
        PemType type = PemType.getInstance();

        // when
        boolean result = Pattern.compile(type.getRegex()).matcher(paramToCheck).matches();

        // then
        assertThat(result).isEqualTo(expected);
    }

    private static String readFile(String filename) throws Exception {
        URI fileURI = PemTypeTest.class
                .getClassLoader()
                .getResource(filename)
                .toURI();
        Path filePath = new File(fileURI).toPath();
        return Files.readAllLines(filePath, StandardCharsets.UTF_8)
                .stream()
                .collect(Collectors.joining("\n"));
    }
}
