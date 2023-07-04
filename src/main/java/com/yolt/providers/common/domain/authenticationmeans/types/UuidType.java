package com.yolt.providers.common.domain.authenticationmeans.types;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.yolt.providers.common.domain.authenticationmeans.AuthenticationMeanType;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@JsonTypeName("UUID")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class UuidType implements AuthenticationMeanType {

    private static final UuidType INSTANCE = new UuidType();
    private static final String FORMAT_DESCRIPTION = "UUID";
    private static final Pattern UUID_PATTERN = Pattern.compile("^[0-9A-Fa-f]{8}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{12}$");

    public static UuidType getInstance() {
        return UuidType.INSTANCE;
    }

    @Override
    public String getDisplayName() {
        return FORMAT_DESCRIPTION;
    }

    @Override
    public String getRegex() {
        return UUID_PATTERN.pattern();
    }

    @Override
    public boolean isStringValid(final String stringToValidate) {
        return UUID_PATTERN.matcher(stringToValidate).matches();
    }
}