package com.yolt.providers.common.domain.externalids;

import com.yolt.providers.common.ProviderKey;
import lombok.NonNull;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
public class ProviderExternalUserIds {

    @NonNull
    UUID clientId;

    @NonNull
    UUID batchId;

    @NonNull
    ProviderKey provider;

    @NonNull
    List<String> externalUserIds;

    @NonNull
    Boolean isLast; // should be reference type (otherwise lombok generates getter/setter with incorrect names)
}
