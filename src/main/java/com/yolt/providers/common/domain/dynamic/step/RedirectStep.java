package com.yolt.providers.common.domain.dynamic.step;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.hibernate.validator.constraints.URL;
import org.springframework.lang.Nullable;

@Getter
@EqualsAndHashCode(callSuper = true)
public class RedirectStep extends Step {
    /**
     * The external consent id is an optional field and only used for openbanking (at the moment).
     */
    @Nullable
    private final String externalConsentId;
    @URL
    @NonNull
    private final String redirectUrl;

    public RedirectStep(@JsonProperty("redirectUrl") String redirectUrl, @JsonProperty("externalConsentId") String externalConsentId, @JsonProperty("providerState") String providerState) {
        super(providerState);
        this.redirectUrl = redirectUrl;
        this.externalConsentId = externalConsentId;
    }

    public RedirectStep(@JsonProperty("redirectUrl") String redirectUrl) {
        this(redirectUrl, null, null);
        if (redirectUrl == null) {
            throw new IllegalArgumentException("A RedirectStep needs a non-null redirect url");
        }

    }
}
