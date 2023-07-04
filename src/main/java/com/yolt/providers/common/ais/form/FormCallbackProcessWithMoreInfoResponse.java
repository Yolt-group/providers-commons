package com.yolt.providers.common.ais.form;

import lombok.Value;
import nl.ing.lovebird.providershared.callback.MoreInfo;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@Value
public class FormCallbackProcessWithMoreInfoResponse {
    @NonNull
    private final String body;

    /**
     * subPath Literally, the sub path of the callback. For example in http://yolt.com/callback/SALTEDGE/interactive it's 'interactive'.
     */
    @Nullable
    private final String subPath;
    /**
     * An object with some more info that might be necessary in order to process the body.
     */
    @NonNull
    final MoreInfo moreInfo;
    @NonNull
    private final AuthenticationDetails authenticationDetails;
}
