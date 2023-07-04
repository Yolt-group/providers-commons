package com.yolt.providers.common.ais.form;

import lombok.Value;

@Value
public class FormDeleteExternalUserByIdRequest {
    private final String externalUserId;
    private final AuthenticationDetails authenticationDetails;

}
