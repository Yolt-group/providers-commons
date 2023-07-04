package com.yolt.providers.common.domain.authenticationmeans.types;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class CertificatesChainPemTypeTest {

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
    private static final String PEM_WITH_THREE_EQUAL_CHARACTERS_AT_THE_END;
    private static final String CERTIFICATE_WITH_NEW_LINES_AFTER_CERTIFICATE;
    private static final String CERTIFICATE_WITH_NEW_LINES_BEFORE_CERTIFICATE;
    private static final String CERTIFICATES_CHAIN;
    private static final String CERTIFICATES_CHAIN_WITH_INVALID_SEPARATION;
    private static final String CERTIFICATES_CHAIN_WITH_NEW_LINES_BETWEEN_CERTIFICATES;
    private static final String CERTIFICATES_CHAIN_WITH_NEW_LINES_AFTER_LAST_AND_BEFORE_FIRST_CERTIFICATE;
    private static final String CERTIFICATES_CHAIN_WITH_MORE_THAN_10_CERTIFICATES;
    private static final String CERTIFICATES_CHAIN_WITH_WRONG_ORDER;

    static {
        try {
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
            PEM_WITH_THREE_EQUAL_CHARACTERS_AT_THE_END = readFile("PEM_WITH_THREE_EQUAL_CHARACTERS_AT_THE_END");
            CERTIFICATE_WITH_NEW_LINES_AFTER_CERTIFICATE = readFile("CERTIFICATE_WITH_NEW_LINES_AFTER_CERTIFICATE");
            CERTIFICATE_WITH_NEW_LINES_BEFORE_CERTIFICATE = readFile("CERTIFICATE_WITH_NEW_LINES_BEFORE_CERTIFICATE");
            CERTIFICATES_CHAIN = readFile("CERTIFICATES_CHAIN");
            CERTIFICATES_CHAIN_WITH_INVALID_SEPARATION = readFile("CERTIFICATES_CHAIN_WITH_INVALID_SEPARATION");
            CERTIFICATES_CHAIN_WITH_NEW_LINES_BETWEEN_CERTIFICATES = readFile("CERTIFICATES_CHAIN_WITH_NEW_LINES_BETWEEN_CERTIFICATES");
            CERTIFICATES_CHAIN_WITH_NEW_LINES_AFTER_LAST_AND_BEFORE_FIRST_CERTIFICATE = readFile("CERTIFICATES_CHAIN_WITH_NEW_LINES_AFTER_LAST_AND_BEFORE_FIRST_CERTIFICATE");
            CERTIFICATES_CHAIN_WITH_MORE_THAN_10_CERTIFICATES = readFile("CERTIFICATES_CHAIN_WITH_MORE_THAN_10_CERTIFICATES");
            CERTIFICATES_CHAIN_WITH_WRONG_ORDER = readFile("CERTIFICATES_CHAIN_WITH_WRONG_ORDER");
        } catch (Exception e) {
            fail("could not read PEM file.");
            throw new RuntimeException("just a runtime exception for the compiler.. We fail anyways.", e);
        }
    }

    private static Stream<Arguments> stringData() {
        return Stream.of(
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
                Arguments.of(PEM_WITH_THREE_EQUAL_CHARACTERS_AT_THE_END, false),
                Arguments.of(CERTIFICATE_WITH_NEW_LINES_AFTER_CERTIFICATE, true),
                Arguments.of(CERTIFICATE_WITH_NEW_LINES_BEFORE_CERTIFICATE, true),
                Arguments.of(CERTIFICATES_CHAIN, true),
                Arguments.of(CERTIFICATES_CHAIN_WITH_INVALID_SEPARATION, false),
                Arguments.of(CERTIFICATES_CHAIN_WITH_NEW_LINES_BETWEEN_CERTIFICATES, false),
                Arguments.of(CERTIFICATES_CHAIN_WITH_NEW_LINES_AFTER_LAST_AND_BEFORE_FIRST_CERTIFICATE, true),
                Arguments.of(CERTIFICATES_CHAIN_WITH_MORE_THAN_10_CERTIFICATES, false),
                Arguments.of(CERTIFICATES_CHAIN_WITH_WRONG_ORDER, false),
                Arguments.of("invalid value", false),
                Arguments.of("", false),
                Arguments.of(" ", false)
        );
    }

    private static Stream<Arguments> patternData() {
        return Stream.of(
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
                Arguments.of(PEM_WITH_THREE_EQUAL_CHARACTERS_AT_THE_END, false),
                Arguments.of(CERTIFICATE_WITH_NEW_LINES_AFTER_CERTIFICATE, true),
                Arguments.of(CERTIFICATE_WITH_NEW_LINES_BEFORE_CERTIFICATE, true),
                Arguments.of(CERTIFICATES_CHAIN, true),
                Arguments.of(CERTIFICATES_CHAIN_WITH_INVALID_SEPARATION, false),
                Arguments.of(CERTIFICATES_CHAIN_WITH_NEW_LINES_BETWEEN_CERTIFICATES, false),
                Arguments.of(CERTIFICATES_CHAIN_WITH_NEW_LINES_AFTER_LAST_AND_BEFORE_FIRST_CERTIFICATE, true),
                Arguments.of(CERTIFICATES_CHAIN_WITH_MORE_THAN_10_CERTIFICATES, true),
                Arguments.of(CERTIFICATES_CHAIN_WITH_WRONG_ORDER, true),
                Arguments.of("invalid value", false),
                Arguments.of("", false),
                Arguments.of(" ", false)
        );
    }

    @ParameterizedTest
    @MethodSource("stringData")
    void shouldValidStringForCertificatesChainPemType(String paramToCheck, boolean expected) {
        // given
        CertificatesChainPemType type = CertificatesChainPemType.getInstance();

        // when
        boolean result = type.isStringValid(paramToCheck);

        // then
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("patternData")
    void shouldPatternMatchForCertificatesChainPemType(String paramToCheck, boolean expected) {
        // given
        CertificatesChainPemType type = CertificatesChainPemType.getInstance();

        // when
        boolean result = Pattern.compile(type.getRegex()).matcher(paramToCheck).matches();

        // then
        assertThat(result).isEqualTo(expected);
    }

    private static String readFile(String filename) throws Exception {
        URI fileURI = Objects.requireNonNull(CertificatesChainPemTypeTest.class
                .getClassLoader()
                .getResource(filename))
                .toURI();

        Path filePath = new File(fileURI).toPath();
        return String.join("\n", Files.readAllLines(filePath, StandardCharsets.UTF_8));
    }
}