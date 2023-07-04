package com.yolt.providers.common.pis.sepa;

import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class DynamicFields {
    @Nullable
    private String creditorAgentName;
    @Nullable
    private String creditorAgentBic;
    @Nullable
    private String remittanceInformationStructured;
    @Nullable
    private String creditorPostalAddressLine;
    @Nullable
    private String creditorPostalCountry;
    @Nullable
    private String debtorName;
}
