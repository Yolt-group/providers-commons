package com.yolt.providers.common.ais.form;

import lombok.Data;

import java.util.UUID;

@Data
public class FormDeleteUserSiteRequest {

    private final String accessMeans;
    private final String userSiteExternalId;
    private final UUID userId;
    private final AuthenticationDetails details;

}
