package com.yolt.providers.common.ais;

import lombok.Data;
import nl.ing.lovebird.providerdomain.ProviderAccountDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class DataProviderResponse {

    private final List<ProviderAccountDTO> accounts;

    public DataProviderResponse() {
        this.accounts = new ArrayList<>();
    }

    public DataProviderResponse(final List<ProviderAccountDTO> accounts) {
        this.accounts = accounts;
    }

    public static DataProviderResponse getEmptyInstance() {
        return new DataProviderResponse(Collections.emptyList());
    }

    public boolean isDataExpectedAsynchronousViaCallback() {
        return false; // By default not supported. (by API providers)
    }

}
