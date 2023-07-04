package com.yolt.providers.common.domain.logging;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class RawDataCensoringRule {

    @NonNull
    private final String headerName;
    @NonNull
    private final String replacement;

}
