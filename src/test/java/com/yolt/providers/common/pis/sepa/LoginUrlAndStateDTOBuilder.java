package com.yolt.providers.common.pis.sepa;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LoginUrlAndStateDTOBuilder {

    private String loginUrl;
    private String providerState;

    public LoginUrlAndStateDTO build() {
        return new LoginUrlAndStateDTO(loginUrl, providerState);
    }

    public LoginUrlAndStateDTOBuilder setLoginUrl(final String loginUrl) {
        this.loginUrl = loginUrl;
        return this;
    }

    public LoginUrlAndStateDTOBuilder setProviderState(final String providerState) {
        this.providerState = providerState;
        return this;
    }
}
