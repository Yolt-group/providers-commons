package com.yolt.providers.common.cryptography;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HashUtilsTest {

    @Test
    public void shouldConfigureSha256Algorithm() throws SecurityException {
        // given
        String valueToHash = "test";

        // when
        String result = HashUtils.sha256Hash(valueToHash);

        // then
        assertThat(result).isEqualTo("9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08");
    }
}