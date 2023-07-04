package com.yolt.providers.common.ais.form;

import lombok.Value;

import java.util.UUID;

@Value
public class FormCreateNewUserRequest {

    private final UUID userId;
    private final UUID siteId;

    /**
     * Create User is the only operation where there is no user nor site-context so this boolean is only used for stubbing purposes.
     * Yoltbank will not proxy a request to the actual provider when the header 'Test-User=true'.
     * This is not elegant solution, but allows us to use testusers and real users at the same environment on the same time.
     */
    private final boolean isTestUser;
    private final AuthenticationDetails details;

}
