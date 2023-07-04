package com.yolt.providers.common.ais.form;

import lombok.Value;
import nl.ing.lovebird.providershared.form.FilledInUserSiteFormValues;
import nl.ing.lovebird.providershared.form.FormSiteLoginFormDTO;
import nl.ing.lovebird.providershared.form.FormUserSiteDTO;
import nl.ing.lovebird.providershared.form.LoginSucceededDTO;

import java.time.Instant;
import java.util.function.Function;

@Value
public class FormUpdateExternalUserSiteRequest {

    private final String externalSiteId;
    private final FormUserSiteDTO formUserSite;
    private final String accessMeans;
    private final Instant transactionsFetchStartTime;
    private final FilledInUserSiteFormValues filledInUserSiteFormValues;
    private final FormSiteLoginFormDTO formSiteLoginForm;
    private final Function<LoginSucceededDTO, Void> loginSucceededDTOVoidFunction;
    private final AuthenticationDetails details;

}
