package com.yolt.providers.common.ais.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Getter
@EqualsAndHashCode
public class EncryptionDetails {
    /**
     * If true, encryption is used and either one of the below is non-null:
     * - {@link #jweDetails}
     */
    private final boolean encryption;
    private final JWEDetails jweDetails;

    @JsonCreator
    private EncryptionDetails(@JsonProperty("jweDetails") JWEDetails jweDetails) {
        this.jweDetails = jweDetails;
        this.encryption = jweDetails != null;
    }

    /**
     * For BudgetInsight and Embedded flow
     */
    @Data
    public static class JWEDetails {
        /**
         * @deprecated Budget Insight specific - use 'alg' from RsaPublicJWK
         */
        @Deprecated
        final String algorithm; // RSA-OAEP
        final String encryptionMethod; // A256GCM
        /**
         * @deprecated Budget Insight specific - JSON with key details sent as a string
         */
        @Deprecated
        final String publicJSONWebKey;
        final RsaPublicJWK rsaPublicJwk;
    }

    @Data
    public static class RsaPublicJWK {
        final String alg;
        final String kty;
        final String n;
        final String e;
    }

    public static EncryptionDetails of(@NonNull JWEDetails details) {
        return new EncryptionDetails(details);
    }

    public static EncryptionDetails noEncryption() {
        return new EncryptionDetails(null);
    }

}
