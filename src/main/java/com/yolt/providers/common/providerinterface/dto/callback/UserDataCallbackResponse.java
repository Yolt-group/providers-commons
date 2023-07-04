package com.yolt.providers.common.providerinterface.dto.callback;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class UserDataCallbackResponse extends CallbackResponseDTO {

    @NonNull
    String externalUserId;

    @NonNull
    UserSiteData userSiteData;

    @JsonCreator
    public UserDataCallbackResponse(@JsonProperty("provider") @NonNull String provider,
                                    @JsonProperty("externalUserId") @NonNull String externalUserId,
                                    @JsonProperty("userSiteData") @NonNull UserSiteData userSiteData) {
        super(provider);
        this.externalUserId = externalUserId;
        this.userSiteData = userSiteData;
    }
}
