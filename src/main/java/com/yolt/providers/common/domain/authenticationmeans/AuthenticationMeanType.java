package com.yolt.providers.common.domain.authenticationmeans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.yolt.providers.common.domain.authenticationmeans.types.CertificatesChainPemType;
import com.yolt.providers.common.domain.authenticationmeans.types.JwtType;
import com.yolt.providers.common.domain.authenticationmeans.types.NoWhiteCharacterStringType;
import com.yolt.providers.common.domain.authenticationmeans.types.PemType;
import com.yolt.providers.common.domain.authenticationmeans.types.PrefixedHexadecimalType;
import com.yolt.providers.common.domain.authenticationmeans.types.StringType;
import com.yolt.providers.common.domain.authenticationmeans.types.UuidType;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "name",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CertificatesChainPemType.class, name = "PEM"),  //TODO: C4PO-6656 - handle name duplicates
        @JsonSubTypes.Type(value = JwtType.class, name = "Jwt"),
        @JsonSubTypes.Type(value = NoWhiteCharacterStringType.class, name = "NoWhiteCharacterString"),
        @JsonSubTypes.Type(value = PemType.class, name = "PEM"),
        @JsonSubTypes.Type(value = PrefixedHexadecimalType.class, name = "PrefixedHexadecimal"),
        @JsonSubTypes.Type(value = StringType.class, name = "String"),
        @JsonSubTypes.Type(value = UuidType.class, name = "UUID"),
})
@JsonIgnoreProperties(ignoreUnknown = true)
public interface AuthenticationMeanType {

    @JsonIgnore
    String getDisplayName();

    @JsonIgnore
    String getRegex();

    @JsonIgnore
    boolean isStringValid(final String stringToValidate);
}