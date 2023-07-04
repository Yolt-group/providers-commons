package com.yolt.providers.common.domain.authenticationmeans.types;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.yolt.providers.common.domain.authenticationmeans.AuthenticationMeanType;
import com.yolt.providers.common.util.KeyUtil;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.security.cert.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@JsonTypeName("PEM")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class CertificatesChainPemType implements AuthenticationMeanType {

    private static final CertificatesChainPemType INSTANCE = new CertificatesChainPemType();
    private static final int MAX_CERTIFICATES_IN_CHAIN = 10;
    private static final String BEGIN_CERTIFICATE_LABEL = "-----BEGIN CERTIFICATE-----";
    private static final String END_CERTIFICATE_LABEL = "-----END CERTIFICATE-----";
    private static final Pattern CERTIFICATES_CHAIN_PEM_PATTERN;

    static {
        String base64Pattern = "([A-Za-z0-9+/\\r\\n])*(=){0,2}([\\r\\n])*";

        CERTIFICATES_CHAIN_PEM_PATTERN = Pattern.compile(
                "(((^([\\r\\n])*)|([\\r\\n])?)" + BEGIN_CERTIFICATE_LABEL + base64Pattern + END_CERTIFICATE_LABEL + "(([\\r\\n])?|([\\r\\n])*$))+");
    }

    public static CertificatesChainPemType getInstance() {
        return CertificatesChainPemType.INSTANCE;
    }

    @Override
    public String getDisplayName() {
        return "PEM chain";
    }

    @Override
    public String getRegex() {
        return CERTIFICATES_CHAIN_PEM_PATTERN.pattern();
    }

    @Override
    public boolean isStringValid(final String stringToValidate) {
        if (!CERTIFICATES_CHAIN_PEM_PATTERN.matcher(stringToValidate).matches()) {
            return false;
        }
        List<String> certificatesPem = extractCertificatesPem(stringToValidate);
        if (certificatesPem.isEmpty() || (certificatesPem.size() > MAX_CERTIFICATES_IN_CHAIN)) {
            return false;
        }
        try {
            validateX509CertificatesChain(convertToX509Certificates(certificatesPem));
            return true;
        } catch (Exception e) {
            log.info("Validation failed for certificates chain pem type, cause: {}", e.getMessage());
            return false;
        }
    }

    private List<String> extractCertificatesPem(String stringToValidate) {
        Matcher matcher = Pattern
                .compile(Pattern.quote(BEGIN_CERTIFICATE_LABEL) + "(.*?)" + Pattern.quote(END_CERTIFICATE_LABEL))
                .matcher(stringToValidate.replace("\n", ""));

        List<String> certificatesPem = new ArrayList<>();
        while(matcher.find()) {
            certificatesPem.add(matcher.group(1));
        }
        return certificatesPem;
    }

    @SneakyThrows
    private LinkedList<X509Certificate> convertToX509Certificates(List<String> certificatesPem) {
        LinkedList<X509Certificate> certificates = new LinkedList<>();
        for (String certificatePem : certificatesPem) {
            certificates.add(KeyUtil.createCertificateFromPemFormat(certificatePem));
        }
        return certificates;
    }

    @SneakyThrows
    private void validateX509CertificatesChain(LinkedList<X509Certificate> certificates) {
        validateX509CertificateRoot(certificates.getLast());

        Set<TrustAnchor> trustAnchors = Collections
                .singleton(new TrustAnchor(certificates.getLast(), null));

        PKIXParameters params = new PKIXParameters(trustAnchors);
        params.setRevocationEnabled(false);

        CertPath certPath = CertificateFactory.getInstance("X.509", "BC")
                .generateCertPath(certificates);

        CertPathValidator.getInstance("PKIX", "BC")
                .validate(certPath, params);
    }

    @SneakyThrows
    private void validateX509CertificateRoot(X509Certificate certificate) {
        certificate.verify(certificate.getPublicKey());
    }
}
