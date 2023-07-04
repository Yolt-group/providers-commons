package com.yolt.providers.common.domain.authenticationmeans.types;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.yolt.providers.common.domain.authenticationmeans.AuthenticationMeanType;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.lang.JoseException;

import java.util.regex.Pattern;

@Slf4j
@JsonTypeName("Jwt")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class JwtType implements AuthenticationMeanType {

    private static final JwtType INSTANCE = new JwtType();
    private static final String FORMAT_DESCRIPTION = "JWT String";
    private static final Pattern STRING_PATTERN = Pattern.compile("^[a-zA-Z0-9\\-_]+\\.[a-zA-Z0-9\\-_]+\\.[a-zA-Z0-9\\-_]+$");

    public static JwtType getInstance() {
        return JwtType.INSTANCE;
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
    public boolean isStringValid(String stringToValidate) {
        boolean matchesRegex = STRING_PATTERN.matcher(stringToValidate).matches();
        return matchesRegex && canParseJwt(stringToValidate);
    }

    private boolean canParseJwt(String jwt) {
        try {
            JsonWebSignature.fromCompactSerialization(jwt);
            return true;
        } catch (JoseException e) {
            log.warn("Couldn't parse JWT");
            return false;
        }
    }
}
