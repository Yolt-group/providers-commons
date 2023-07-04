package com.yolt.providers.common.ais.form;

import lombok.Value;
import nl.ing.lovebird.providershared.AccessMeansDTO;
import nl.ing.lovebird.providershared.form.FormUserSiteDTO;
import nl.ing.lovebird.providershared.form.LoginSucceededDTO;

import java.time.Instant;
import java.util.function.Function;


@Value
public class FormFetchDataRequest {

    private final String externalSiteId;
    private final FormUserSiteDTO formUserSite;
    private final Instant transactionsFetchStartTime;
    private final AccessMeansDTO accessMeans;
    private final Function<LoginSucceededDTO, Void> setLoginSucceededFunction;
    private final boolean forFlywheel;
    private final AuthenticationDetails details;

}

