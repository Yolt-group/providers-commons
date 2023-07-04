package com.yolt.providers.common.ais.form;

import lombok.Value;
import nl.ing.lovebird.providershared.AccessMeansDTO;

@Value
public class FormDeleteUserRequest {

    private final AccessMeansDTO accessMeans;
    private final AuthenticationDetails details;

}
