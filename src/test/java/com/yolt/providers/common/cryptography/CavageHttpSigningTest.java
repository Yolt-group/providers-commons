package com.yolt.providers.common.cryptography;

import com.yolt.securityutils.signing.SignatureAlgorithm;
import org.jose4j.jws.JsonWebSignature;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class CavageHttpSigningTest {

    private CavageHttpSigning cavageHttpSigning;
    private final UUID privateKid = UUID.fromString("82725ba0-6781-47d0-b66d-a20a7e4623a3");
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.SHA256_WITH_RSA;

    @BeforeEach
    public void beforeEach() {
        cavageHttpSigning = new CavageHttpSigning(new Signer() {
            @Override
            public JwsSigningResult sign(final JsonWebSignature jws, final UUID privateKid, final SignatureAlgorithm signatureAlgorithm) {
                return null;
            }

            @Override
            public String sign(final byte[] bytesToSign, final UUID privateKid, final SignatureAlgorithm signatureAlgorithm) {
                return "<signed-data>" + new String(bytesToSign) + "</signed-data>";
            }
        }, privateKid, signatureAlgorithm);
    }

    @Test
    public void shouldSignHeaders() {
        // given
        final HashMap<String, String> headers = new HashMap<>();
        headers.put("PSU-IP-Address", "127.0.0.1");
        headers.put("X-Request-Id", "uuid");
        headers.put("Digest", "hash");

        final String headerKeyId = "ourKeyIdentifier";

        // when
        final String signedHeaders = cavageHttpSigning.signHeaders(headers, headerKeyId, "POST", "/some/path");

        // then
        assertThat(signedHeaders).isEqualTo("keyId=\"" + headerKeyId +
                "\", algorithm=\"" + signatureAlgorithm.getHttpSignatureAlgorithm() +
                "\", headers=\"(request-target) x-request-id digest psu-ip-address\", signature=\"<signed-data>(request-target): post /some/path\nx-request-id: uuid\ndigest: hash\npsu-ip-address: 127.0.0.1</signed-data>\"");
    }
}
