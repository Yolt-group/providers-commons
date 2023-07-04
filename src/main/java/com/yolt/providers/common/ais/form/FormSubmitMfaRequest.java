package com.yolt.providers.common.ais.form;

import lombok.Value;
import nl.ing.lovebird.providershared.form.FilledInUserSiteFormValues;
import nl.ing.lovebird.providershared.form.FormUserSiteDTO;
import nl.ing.lovebird.providershared.form.LoginSucceededDTO;
import nl.ing.lovebird.providershared.form.SetExternalUserSiteIdDTO;

import java.time.Instant;
import java.util.function.Function;

@Value
public class FormSubmitMfaRequest {

    private final String externalSiteId;
    private final FormUserSiteDTO formUserSite;
    private final String accessMeans;
    private final Instant transactionsFetchStartTime;
    private final String mfaFormJson;
    private final FilledInUserSiteFormValues filledInUserSiteFormValues;
    private final Function<LoginSucceededDTO, Void> loginSucceededFunction;
    private final Function<SetExternalUserSiteIdDTO, Void> setExternalUserSiteIdDTOVoidFunction;
    private final AuthenticationDetails details;

}
