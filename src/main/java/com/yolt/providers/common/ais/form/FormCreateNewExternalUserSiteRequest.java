package com.yolt.providers.common.ais.form;

import lombok.Value;
import nl.ing.lovebird.providershared.AccessMeansDTO;
import nl.ing.lovebird.providershared.form.FilledInUserSiteFormValues;
import nl.ing.lovebird.providershared.form.FormSiteLoginFormDTO;
import nl.ing.lovebird.providershared.form.LoginSucceededDTO;
import nl.ing.lovebird.providershared.form.SetExternalUserSiteIdDTO;

import java.time.Instant;
import java.util.UUID;
import java.util.function.Function;

@Value
public class FormCreateNewExternalUserSiteRequest {

    private final String externalSiteId;
    private final UUID userId;
    private final UUID userSiteId;
    private final AccessMeansDTO accessMeans;
    private final Instant transactionsFetchStartTime;
    private final FilledInUserSiteFormValues filledInUserSiteFormValues;
    private final FormSiteLoginFormDTO formSiteLoginForm;
    private final Function<LoginSucceededDTO, Void> loginSucceededFunction;
    private final Function<SetExternalUserSiteIdDTO, Void> setExternalUserSiteIdFunction;
    private final AuthenticationDetails details;

}
