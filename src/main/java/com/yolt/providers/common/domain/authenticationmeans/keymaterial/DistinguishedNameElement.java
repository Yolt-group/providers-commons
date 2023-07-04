package com.yolt.providers.common.domain.authenticationmeans.keymaterial;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class DistinguishedNameElement {
    private final String type;
    private final String value;
    private final String placeholder;
    private final boolean editable;

    // Not using lombok to create this, because then it will generate conflicting constructors for Jackson.
    public DistinguishedNameElement(String type) {
        this.type = type;
        this.value = "";
        this.placeholder = "";
        this.editable = true;
    }
}
