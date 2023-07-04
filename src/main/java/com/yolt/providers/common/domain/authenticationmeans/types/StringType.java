package com.yolt.providers.common.domain.authenticationmeans.types;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.yolt.providers.common.domain.authenticationmeans.AuthenticationMeanType;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@JsonTypeName("String")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class StringType implements AuthenticationMeanType {

    private static final StringType INSTANCE = new StringType();
    private static final String FORMAT_DESCRIPTION = "String";

    public static StringType getInstance() {
        return StringType.INSTANCE;
    }

    @Override
    public String getDisplayName() {
        return FORMAT_DESCRIPTION;
    }

    /**
     * {@link StringType} is not validated with a regular expression.
     * @return null
     */
    @Override
    public String getRegex() {
        return null;
    }

    /**
     * {@link StringType} allows any string that is not blank.
     * @param stringToValidate, the string to validate
     * @return true if the string is not blank, false otherwise.
     */
    @Override
    public boolean isStringValid(final String stringToValidate) {
        return StringUtils.isNotBlank(stringToValidate);
    }
}