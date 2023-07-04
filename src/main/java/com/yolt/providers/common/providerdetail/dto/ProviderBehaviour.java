package com.yolt.providers.common.providerdetail.dto;

import lombok.Getter;

/**
 * Denotes what kind of additional behaviour provider is supporting.
 */
public enum ProviderBehaviour {

    /**
     * The provider can't work without an externalUserSiteId
     * Example: Yodlee definitely needs an externalUserSiteId, because they do not identify the user-site by session-token where the others
     * do.
     */
    EXTERNAL_ID_REQUIRED,

    /**
     * The provider can return an externalUserSiteId, but does not rely on it.
     * Example: Starlingbank can return an externalUserSiteId, but doesn't use this identifier to perform other operations.
     * Thus, it can work without an externalUserSiteId (uses an accessToken), but is able to provide one.
     */
    EXTERNAL_ID_SUPPORTED,

    /**
     * The provider supports the state parameter in the redirectUrl. This makes the siteId in the redirectUrl unnecessary.
     */
    STATE("state"),

    CALLBACK,

    /**
     * The provider implements Polish API getAccounts endpoint
     */
    GET_ACCOUNTS;

    @Getter
    private final String parameterValue;

    ProviderBehaviour() {
        this(null);
    }

    ProviderBehaviour(final String parameterValue) {
        this.parameterValue = parameterValue;
    }
}
