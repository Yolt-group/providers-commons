package com.yolt.providers.common.domain;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class ProviderInternalDocumentationTest {

    @Test
    public void shouldThrowInvalidArgumentExceptionWhenPathIsNull() {
        // given
        String path = null;

        // when
        ThrowableAssert.ThrowingCallable callable = () -> ProviderInternalDocumentation.readDocumentationByPath(path);

        // then
        assertThatThrownBy(callable).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldThrowInvalidArgumentExceptionWhenExtensionIsInvalid() {
        // given
        String path = "something.notExisting";

        // when
        ThrowableAssert.ThrowingCallable callable = () -> ProviderInternalDocumentation.readDocumentationByPath(path);

        // then
        assertThatThrownBy(callable).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldThrowInvalidArgumentExceptionWhenPathIsInvalid() {
        // given
        String path = "not/existing/path.md";

        // when
        ThrowableAssert.ThrowingCallable callable = () -> ProviderInternalDocumentation.readDocumentationByPath(path);

        // then
        assertThatThrownBy(callable).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldRetrieveProperDocumentationWhenPathIsValid() {
        // given
        String path = "src/test/resources/SAMPLE_README.md";

        // when
        ProviderInternalDocumentation providerInternalDocumentation = ProviderInternalDocumentation.readDocumentationByPath(path);

        // then
        String encodedContent = providerInternalDocumentation.getEncodedContent();
        assertThat(encodedContent).isNotBlank();
        byte[] decode = Base64.getDecoder().decode(encodedContent);
        String documentation = new String(decode, StandardCharsets.UTF_8).trim();
        assertThat(documentation).isEqualTo("Sample readme documentation.");
    }
}
