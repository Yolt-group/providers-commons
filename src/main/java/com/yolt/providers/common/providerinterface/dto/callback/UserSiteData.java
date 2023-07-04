package com.yolt.providers.common.providerinterface.dto.callback;

import lombok.NonNull;
import lombok.Value;
import nl.ing.lovebird.providerdomain.ProviderAccountDTO;
import nl.ing.lovebird.providershared.ProviderServiceResponseStatusValue;

import java.util.List;

@Value
public class UserSiteData {

    @NonNull String externalUserSiteId;
    @NonNull List<ProviderAccountDTO> accounts;
    @NonNull ProviderServiceResponseStatusValue providerServiceResponseStatusValue;
}
