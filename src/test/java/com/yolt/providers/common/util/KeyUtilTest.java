package com.yolt.providers.common.util;

import com.yolt.providers.common.domain.authenticationmeans.types.CertificatesChainPemTypeTest;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class KeyUtilTest {

    private static final String CERTIFICATES_CHAIN_WITH_NEW_LINES_AFTER_LAST_AND_BEFORE_FIRST_CERTIFICATE;

    static {
        try {
            CERTIFICATES_CHAIN_WITH_NEW_LINES_AFTER_LAST_AND_BEFORE_FIRST_CERTIFICATE = readFile("CERTIFICATES_CHAIN_WITH_NEW_LINES_AFTER_LAST_AND_BEFORE_FIRST_CERTIFICATE");
        } catch (Exception e) {
            fail("could not read PEM file.");
            throw new RuntimeException("just a runtime exception for the compiler.. We fail anyways.", e);
        }
    }

    @Test
    public void shouldCreateCertificatesChainFromPemFormat() throws CertificateException {
        // given
        String file = CERTIFICATES_CHAIN_WITH_NEW_LINES_AFTER_LAST_AND_BEFORE_FIRST_CERTIFICATE;

        // when
        X509Certificate[] certificatesChain = KeyUtil.createCertificatesChainFromPemFormat(file);

        // then
        assertThat(certificatesChain).hasSize(3);
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
