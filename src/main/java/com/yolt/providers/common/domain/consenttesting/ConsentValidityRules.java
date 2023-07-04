package com.yolt.providers.common.domain.consenttesting;

import lombok.Data;

import java.util.Collections;
import java.util.Set;

@Data
public class ConsentValidityRules {

    public static final ConsentValidityRules EMPTY_RULES_SET = new ConsentValidityRules(Collections.emptySet());

    /**
     * Set of keywords that should be present on bank login html page.
     */
    private final Set<String> keywords;
}
