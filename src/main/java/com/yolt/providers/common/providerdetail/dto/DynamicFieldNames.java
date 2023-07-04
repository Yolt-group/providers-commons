package com.yolt.providers.common.providerdetail.dto;

import lombok.Getter;

public enum DynamicFieldNames {

    CREDITOR_AGENT_BIC("creditorAgentBic"),
    CREDITOR_AGENT_NAME("creditorAgentName"),
    REMITTANCE_INFORMATION_STRUCTURED("remittanceInformationStructured"),
    CREDITOR_POSTAL_ADDRESS_LINE("creditorPostalAddressLine"),
    CREDITOR_POSTAL_COUNTRY("creditorPostalCountry"),
    DEBTOR_NAME("debtorName");

    @Getter
    private String value;

    DynamicFieldNames(String value) {
        this.value = value;
    }

}
