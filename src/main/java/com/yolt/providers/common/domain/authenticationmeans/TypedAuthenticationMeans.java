package com.yolt.providers.common.domain.authenticationmeans;

import com.yolt.providers.common.domain.authenticationmeans.types.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.yolt.providers.common.domain.authenticationmeans.RenderingType.*;

@AllArgsConstructor
@Getter
public class TypedAuthenticationMeans {
    // For backwards compatibility: This was an enum first..
    public static final TypedAuthenticationMeans AUDIENCE_STRING = new TypedAuthenticationMeans("Audience", NoWhiteCharacterStringType.getInstance(), ONE_LINE_STRING);
    public static final TypedAuthenticationMeans CLIENT_ID_UUID = new TypedAuthenticationMeans("Client ID", UuidType.getInstance(), ONE_LINE_STRING);
    public static final TypedAuthenticationMeans INSTITUTION_ID_STRING = new TypedAuthenticationMeans("Institution Id", NoWhiteCharacterStringType.getInstance(), ONE_LINE_STRING);
    public static final TypedAuthenticationMeans KEY_ID = new TypedAuthenticationMeans("Key Id", UuidType.getInstance(), ONE_LINE_STRING);
    public static final TypedAuthenticationMeans CERTIFICATE_ID = new TypedAuthenticationMeans("Certificate Id", StringType.getInstance(), ONE_LINE_STRING);
    public static final TypedAuthenticationMeans PRIVATE_KEY_PEM = new TypedAuthenticationMeans("Signing Private Key", PemType.getInstance(), MULTI_LINE_STRING);
    public static final TypedAuthenticationMeans PUBLIC_KEY_PEM = new TypedAuthenticationMeans("Signing Public Key", PemType.getInstance(), MULTI_LINE_STRING);
    public static final TypedAuthenticationMeans CERTIFICATE_PEM = new TypedAuthenticationMeans("Certificate", PemType.getInstance(), MULTI_LINE_STRING);
    public static final TypedAuthenticationMeans CLIENT_SIGNING_CERTIFICATE_PEM = new TypedAuthenticationMeans("Client Signing Certificate", PemType.getInstance(), MULTI_LINE_STRING);
    public static final TypedAuthenticationMeans CLIENT_TRANSPORT_CERTIFICATE_PEM = new TypedAuthenticationMeans("Client Transport Certificate", PemType.getInstance(), MULTI_LINE_STRING);
    public static final TypedAuthenticationMeans API_KEY = new TypedAuthenticationMeans("API Key", PrefixedHexadecimalType.getInstance(), ONE_LINE_STRING);
    public static final TypedAuthenticationMeans API_KEY_STRING = new TypedAuthenticationMeans("API Key", StringType.getInstance(), ONE_LINE_STRING);
    public static final TypedAuthenticationMeans API_SECRET_STRING = new TypedAuthenticationMeans("API Secret", StringType.getInstance(), PASSWORD);
    public static final TypedAuthenticationMeans API_SECRET = new TypedAuthenticationMeans("API Secret", PrefixedHexadecimalType.getInstance(), PASSWORD);
    public static final TypedAuthenticationMeans KEYSTORE_STRING = new TypedAuthenticationMeans("Keystore", StringType.getInstance(), MULTI_LINE_STRING);
    public static final TypedAuthenticationMeans KEYSTORE_ALIAS_STRING = new TypedAuthenticationMeans("Keystore alias", StringType.getInstance(), ONE_LINE_STRING);
    public static final TypedAuthenticationMeans KEYSTORE_PASSWORD_STRING = new TypedAuthenticationMeans("Keystore password", StringType.getInstance(), PASSWORD);
    public static final TypedAuthenticationMeans CLIENT_ID_STRING = new TypedAuthenticationMeans("Client Id", NoWhiteCharacterStringType.getInstance(), ONE_LINE_STRING);
    public static final TypedAuthenticationMeans KEY_ID_HEADER_STRING = new TypedAuthenticationMeans("Key Id", StringType.getInstance(), ONE_LINE_STRING);
    public static final TypedAuthenticationMeans CLIENT_SECRET_STRING = new TypedAuthenticationMeans("Client Secret", StringType.getInstance(), PASSWORD);
    public static final TypedAuthenticationMeans WEB_API_KEY_UUID = new TypedAuthenticationMeans("Web Api Key", UuidType.getInstance(), ONE_LINE_STRING);
    public static final TypedAuthenticationMeans SIGNING_KEY_ID_STRING = new TypedAuthenticationMeans("Signing key header Id", StringType.getInstance(), ONE_LINE_STRING);
    public static final TypedAuthenticationMeans TPP_ID = new TypedAuthenticationMeans("Tpp Id", StringType.getInstance(), ONE_LINE_STRING);
    public static final TypedAuthenticationMeans ORGANIZATION_ID_STRING = new TypedAuthenticationMeans("Organization Id", NoWhiteCharacterStringType.getInstance(), ONE_LINE_STRING);
    public static final TypedAuthenticationMeans SOFTWARE_ID_STRING = new TypedAuthenticationMeans("Software Id", NoWhiteCharacterStringType.getInstance(), ONE_LINE_STRING);
    public static final TypedAuthenticationMeans SOFTWARE_STATEMENT_ASSERTION_STRING = new TypedAuthenticationMeans("Software Statement Assertion", JwtType.getInstance(), ONE_LINE_STRING);
    public static final TypedAuthenticationMeans CLIENT_EMAIL = new TypedAuthenticationMeans("Client e-mail", NoWhiteCharacterStringType.getInstance(), ONE_LINE_STRING);
    public static final TypedAuthenticationMeans ISSUER_STRING = new TypedAuthenticationMeans("Issuer", StringType.getInstance(), ONE_LINE_STRING);
    public static final TypedAuthenticationMeans CLIENT_SIGNING_CERTIFICATES_CHAIN_PEM = new TypedAuthenticationMeans("Client Signing Certificates Chain", CertificatesChainPemType.getInstance(), MULTI_LINE_STRING);
    public static final TypedAuthenticationMeans CLIENT_TRANSPORT_CERTIFICATES_CHAIN_PEM = new TypedAuthenticationMeans("Client Transport Certificates Chain", CertificatesChainPemType.getInstance(), MULTI_LINE_STRING);
    public static final TypedAuthenticationMeans CUSTOMER_CODE_STRING = new TypedAuthenticationMeans("Customer Code", NoWhiteCharacterStringType.getInstance(), ONE_LINE_STRING);
    public static final TypedAuthenticationMeans CUSTOMER_SPONSOR_CODE_STRING = new TypedAuthenticationMeans("Sponsor Code", NoWhiteCharacterStringType.getInstance(), ONE_LINE_STRING);
    public static final TypedAuthenticationMeans CERTIFICATE_AGREEMENT_NUMBER_STRING = new TypedAuthenticationMeans("Certificate agreement number", NoWhiteCharacterStringType.getInstance(), ONE_LINE_STRING);
    public static final TypedAuthenticationMeans ALIAS_STRING = new TypedAuthenticationMeans("Alias", StringType.getInstance(), ONE_LINE_STRING);

    private String displayName;
    private AuthenticationMeanType type;
    private RenderingType rendering;
}