package com.yolt.providers.common.rest;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.client.MockClientHttpResponse;
import org.springframework.web.client.HttpClientErrorException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class ExternalResponseErrorHandlerTest {

    private final ExternalResponseErrorHandler subject = new ExternalResponseErrorHandler();

    @Test
    public void shouldThrowHttpClientErrorExceptionBadRequestWhenHandleErrorWithNoBody() {
        // given
        MockClientHttpResponse httpResponse = new MockClientHttpResponse(new byte[0], HttpStatus.BAD_REQUEST);

        // when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> subject.handleError(httpResponse);

        // then
        assertThatThrownBy(throwingCallable).isExactlyInstanceOf(HttpClientErrorException.BadRequest.class)
                .hasMessage("400 Bad Request: [no body]");
    }

    @Test
    public void shouldThrowHttpClientErrorExceptionBadRequestWhenHandleErrorWithSensitiveBody() {
        // given
        MockClientHttpResponse httpResponse = new MockClientHttpResponse("Very sensitive body".getBytes(), HttpStatus.BAD_REQUEST);

        // when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> subject.handleError(httpResponse);

        // then
        assertThatThrownBy(throwingCallable).isExactlyInstanceOf(HttpClientErrorException.BadRequest.class)
                .hasMessage("400 Bad Request: [Body length: 19, Check RDD to see content of body]");
    }
}