package com.yolt.providers.common.util;

import com.yolt.providers.common.exception.InvalidUrlException;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


public class PinUrlHelperTest {

    @Test
    public void shouldThrowInvalidUrlExceptionWhenUrlIsNull() {
        // given
        String url = null;

        // when
        ThrowableAssert.ThrowingCallable callable = () -> PinUrlHelper.getPinUrlForHttpsUrl(url);

        // then
        assertThatThrownBy(callable).isExactlyInstanceOf(InvalidUrlException.class);
    }
    
    @Test
    public void shouldThrowExceptionWhenUrlDoesNotHaveHttpsPrefix() {
        // given
        String url = "api.my-domain.com";

        // when
        ThrowableAssert.ThrowingCallable callable = () ->  PinUrlHelper.getPinUrlForHttpsUrl(url);

        // then
        assertThatThrownBy(callable).isExactlyInstanceOf(InvalidUrlException.class);
    }

    @Test
    public void shouldRemoveHttpsAndTrailingPathFromUrl() {
        // given
        String url = "https://api-sandbox.starlingbank.com/api/v1";
        String expectedPinUrl = "api-sandbox.starlingbank.com";

        // when
        String pinUrl = PinUrlHelper.getPinUrlForHttpsUrl(url);

        // then
        assertThat(pinUrl).isEqualTo(expectedPinUrl);
    }
}
