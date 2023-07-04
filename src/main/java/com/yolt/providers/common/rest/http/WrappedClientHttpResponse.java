package com.yolt.providers.common.rest.http;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Objective of this wrapper is to allow us to read the input more than once.
 * This is normally not possible because apache http client closes the stream after encountering EOF.
 * Copied over from {@link org.springframework.http.client.BufferingClientHttpResponseWrapper} because it is not public.
 */
public class WrappedClientHttpResponse implements ClientHttpResponse {

    private final ClientHttpResponse response;

    @Nullable
    private byte[] body;

    public WrappedClientHttpResponse(ClientHttpResponse response) {
        this.response = response;
    }

    @Override
    public HttpStatus getStatusCode() throws IOException {
        return this.response.getStatusCode();
    }

    @Override
    public int getRawStatusCode() throws IOException {
        return this.response.getRawStatusCode();
    }

    @Override
    public String getStatusText() throws IOException {
        return this.response.getStatusText();
    }

    @Override
    public HttpHeaders getHeaders() {
        return this.response.getHeaders();
    }

    @Override
    public InputStream getBody() throws IOException {
        return new ByteArrayInputStream(getBytes());
    }

    public byte[] getBytes() throws IOException {
        if (this.body == null) {
            this.body = StreamUtils.copyToByteArray(this.response.getBody());
        }
        return this.body;
    }

    @Override
    public void close() {
        this.response.close();
    }
}
