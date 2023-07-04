package com.yolt.providers.common.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class YoltProxySelectorBuilder {

    private final String ispProxyHost;
    private final Integer ispProxyPort;

    public YoltProxySelectorBuilder(@Value("${isp.proxy.host}") final String ispProxyHost,
                                    @Value("${isp.proxy.port}") final Integer ispProxyPort) {
        this.ispProxyHost = ispProxyHost;
        this.ispProxyPort = ispProxyPort;
    }

    public YoltProxySelector build(final String serviceName) {
        log.info("Configured {} with proxy {}:{}", serviceName, ispProxyHost, ispProxyPort);
        return new ExternalYoltProxySelector(ispProxyHost, ispProxyPort);
    }
}
