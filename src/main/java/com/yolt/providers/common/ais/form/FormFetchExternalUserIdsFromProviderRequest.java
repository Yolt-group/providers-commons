package com.yolt.providers.common.ais.form;

import com.yolt.providers.common.domain.externalids.ProviderExternalUserIds;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.util.UUID;
import java.util.function.Function;

@Value
public class FormFetchExternalUserIdsFromProviderRequest {
    @NotNull
    UUID clientId;
    Function<ProviderExternalUserIds, Void> publishMessageFunction;
    UUID batchId;
    Integer externalUserIdsSliceLimit;
    AuthenticationDetails details;
}
