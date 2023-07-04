package com.yolt.providers.common.domain.authenticationmeans;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AuthenticationMeanTypeKeyDTO {

    private final String key;
    private final String placeholder;
    private final String displayName;
    private final String regex;
    private final String type;
}
