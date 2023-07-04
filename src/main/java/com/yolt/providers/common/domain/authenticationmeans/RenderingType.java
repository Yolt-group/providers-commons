package com.yolt.providers.common.domain.authenticationmeans;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RenderingType {

    ONE_LINE_STRING("text"),
    MULTI_LINE_STRING("textarea"),
    PASSWORD("password");

    private final String value;
}