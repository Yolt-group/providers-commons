package com.yolt.providers.common.domain.authenticationmeans.types;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.yolt.providers.common.domain.authenticationmeans.AuthenticationMeanType;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@JsonTypeName("PEM")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class PemType implements AuthenticationMeanType {

    private static final String BASE_64_PATTERN = "([A-Za-z0-9+/\\r\\n])*(=){0,2}([\\r\\n])*";
    private static final Pattern PEM_PATTERN = Pattern.compile("^([\\r\\n])*-----BEGIN.*-----" + BASE_64_PATTERN + "-----END.*-----([\\r\\n])*$");
    private static final String FORMAT_DESCRIPTION = "PEM";
    private static final PemType INSTANCE = new PemType();

    public static PemType getInstance() {
        return PemType.INSTANCE;
    }

    @Override
    public String getDisplayName() {
        return FORMAT_DESCRIPTION;
    }

    @Override
    public String getRegex() {
        return PEM_PATTERN.pattern();
    }

    @Override
    public boolean isStringValid(final String stringToValidate) {
        return PEM_PATTERN
                .matcher(stringToValidate)
                .matches();
    }
}