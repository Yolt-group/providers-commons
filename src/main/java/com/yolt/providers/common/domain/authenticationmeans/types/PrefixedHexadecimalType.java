package com.yolt.providers.common.domain.authenticationmeans.types;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.yolt.providers.common.domain.authenticationmeans.AuthenticationMeanType;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@JsonTypeName("PrefixedHexadecimal")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class PrefixedHexadecimalType implements AuthenticationMeanType {

    private static final Pattern PATTERN = Pattern.compile("_?[A-Fa-f0-9]{2,4096}$");

    private static final PrefixedHexadecimalType INSTANCE = new PrefixedHexadecimalType();

    public static PrefixedHexadecimalType getInstance() {
        return PrefixedHexadecimalType.INSTANCE;
    }

    @Override
    public String getDisplayName() {
        return "Hexadecimal string with optional prefix";
    }

    @Override
    public String getRegex() {
        return PATTERN.pattern();
    }

    @Override
    public boolean isStringValid(final String stringToValidate) {
        return PATTERN.matcher(stringToValidate).matches();
    }
}