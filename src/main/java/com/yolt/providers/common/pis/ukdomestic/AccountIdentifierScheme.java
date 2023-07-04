package com.yolt.providers.common.pis.ukdomestic;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum AccountIdentifierScheme {
    @JsonProperty("IBAN")
    IBAN("IBAN"),
    @JsonProperty("SortCodeAccountNumber")
    SORTCODEACCOUNTNUMBER("SortCodeAccountNumber");

    String value;

    AccountIdentifierScheme(String value) {
        this.value = value;
    }
}
