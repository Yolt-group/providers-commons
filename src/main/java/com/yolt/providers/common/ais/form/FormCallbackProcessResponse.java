package com.yolt.providers.common.ais.form;

import lombok.Value;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@Value
public class FormCallbackProcessResponse {
    @NonNull
    private final String body;
    /**
     * subPath Literally, the sub path of the callback. For example in http://yolt.com/callback/SALTEDGE/interactive it's 'interactive'.
     */
    @Nullable
    private final String subPath;
    @NonNull
    private final AuthenticationDetails authenticationDetails;
}
