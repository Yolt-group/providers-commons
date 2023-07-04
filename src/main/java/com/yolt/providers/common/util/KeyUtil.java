package com.yolt.providers.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import java.io.ByteArrayInputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.quote;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KeyUtil {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static String stripKeyFromBeginEndTags(final String key, final String tag) {
        return key.replaceAll("(-----(BEGIN|END) " + tag + "-----)?", "");
    }

    public static String wrapInBeginEndTag(final String keyword, final String value) {
        return "-----BEGIN " + keyword + "-----" + value + "-----END " + keyword + "-----";
    }

    public static PrivateKey createPrivateKeyFromPemFormat(final String privateKeyPem) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String privateKeyWithoutBeginEndTags =
                StringUtils.deleteWhitespace(stripKeyFromBeginEndTags(privateKeyPem, "PRIVATE KEY"));
        byte[] bytes = Base64.decode(privateKeyWithoutBeginEndTags);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(keySpec);
    }

    public static X509Certificate createCertificateFromPemFormat(final String certificateString) throws CertificateException {
        String certificate = StringUtils.deleteWhitespace(KeyUtil.stripKeyFromBeginEndTags(certificateString, "CERTIFICATE"));
        byte[] decodedCert = Base64.decode(certificate);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(decodedCert);
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509", Security.getProvider("BC"));
        X509Certificate x509Certificate = (X509Certificate) certFactory.generateCertificate(inputStream);
        if (x509Certificate == null) {
            throw new CertificateException("Invalid certificate");
        }
        return x509Certificate;
    }

    public static X509Certificate[] createCertificatesChainFromPemFormat(final String certificatesChainString) throws CertificateException {
        Matcher matcher = Pattern
                .compile(quote("-----BEGIN CERTIFICATE-----") + "(.*?)" + quote("-----END CERTIFICATE-----"))
                .matcher(certificatesChainString.replace("\n", ""));

        List<X509Certificate> certificates = new ArrayList<>();
        while (matcher.find()) {
            certificates.add(createCertificateFromPemFormat(matcher.group(1)));
        }
        return certificates.toArray(new X509Certificate[0]);
    }

    public static RSAPublicKey createPublicKeyFromPemFormat(final String encodedPublicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String publicKey = StringUtils.deleteWhitespace(KeyUtil.stripKeyFromBeginEndTags(encodedPublicKey, "PUBLIC KEY"));
        byte[] bytes = java.util.Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec x509Key = new X509EncodedKeySpec(bytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) kf.generatePublic(x509Key);
    }
}
