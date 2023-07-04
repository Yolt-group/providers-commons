package com.yolt.providers.common.ais.form;

import lombok.Value;

@Value
public class FormFetchLoginFormRequest {

    private final String siteExternalId;
    private final AuthenticationDetails details;

}
