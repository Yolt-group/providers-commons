package com.yolt.providers.common.ais.form;

import lombok.Value;
import nl.ing.lovebird.providershared.AccessMeansDTO;

@Value
public class FormCreateNewUserResponse {

    private final AccessMeansDTO accessMeans;
    private final String externalUserId;

}
