package com.yolt.providers.common.rest.http;

import io.micrometer.core.instrument.util.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WrappedClientHttpResponseTest {

    private static final String BODY = "body";

    @Spy
    private ClientHttpResponse responseSpy;

    @BeforeEach
    public void beforeEach() throws IOException {
        when(responseSpy.getBody()).thenReturn(new ByteArrayInputStream(BODY.getBytes()));
    }

    @Test
    public void shouldNotBeAbleToReadBodyTwiceForRegularResponse() throws IOException {
        // when
        String firstResult = IOUtils.toString(responseSpy.getBody(), Charset.defaultCharset());
        String secondResult = IOUtils.toString(responseSpy.getBody(), Charset.defaultCharset());

        // then
        // First read from stream (never causes problems)
        assertThat(firstResult).isEqualTo(BODY);
        // Second read from stream (will not be possible with usual response)
        assertThat(secondResult).isEqualTo("");
    }

    @Test
    public void shouldBeAbleToReadBodyTwiceForWrappedResponse() throws IOException {
        // given
        WrappedClientHttpResponse wrappedResponse = new WrappedClientHttpResponse(responseSpy);

        // when
        String firstResult = IOUtils.toString(wrappedResponse.getBody(), Charset.defaultCharset());
        String secondResult = IOUtils.toString(wrappedResponse.getBody(), Charset.defaultCharset());

        // then
        // First read from stream (never causes problems)
        assertThat(firstResult).isEqualTo(BODY);
        // Second read from stream (our WrappedClientHttpResponse should make that work)
        assertThat(secondResult).isEqualTo(BODY);
    }

}