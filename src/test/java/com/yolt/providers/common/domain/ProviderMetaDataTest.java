package com.yolt.providers.common.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProviderMetaDataTest {

    @Test
    public void shouldBuildProviderMetadataWithDefaultValues() {
        //when
        ProviderMetaData metaData = ProviderMetaData.builder().build();

        // then
        assertThat(metaData.maximumRedirectUrlsPerAuthenticationMeans).isNull();
    }
}
