package com.yolt.providers.common.rest;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.SocketAddress;
import java.net.URI;

@Slf4j
class ExternalYoltProxySelector extends YoltProxySelector {

    ExternalYoltProxySelector(String ispProxyHost, Integer ispProxyPort) {
        super(ispProxyHost, ispProxyPort);
    }

    @Override
    public void connectFailed(URI uri, SocketAddress sa, IOException e) {
        log.warn("Connection via proxy failed.", e);
    }
}