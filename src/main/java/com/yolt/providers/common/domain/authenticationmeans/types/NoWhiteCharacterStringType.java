package com.yolt.providers.common.domain.authenticationmeans.types;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.yolt.providers.common.domain.authenticationmeans.AuthenticationMeanType;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@JsonTypeName("NoWhiteCharacterString")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class NoWhiteCharacterStringType implements AuthenticationMeanType {

    private static final NoWhiteCharacterStringType INSTANCE = new NoWhiteCharacterStringType();
    private static final String FORMAT_DESCRIPTION = "String without white characters";
    private static final Pattern STRING_PATTERN = Pattern.compile("^[^\\s]+$");

    public static NoWhiteCharacterStringType getInstance() {
        return NoWhiteCharacterStringType.INSTANCE;
    }

    @Override
    public String getDisplayName() {
        return FORMAT_DESCRIPTION;
    }

    @Override
    public String getRegex() {
        return STRING_PATTERN.pattern();
    }

    @Override
    public boolean isStringValid(final String stringToValidate) {
        return STRING_PATTERN.matcher(stringToValidate).matches();
    }
}