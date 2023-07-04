package com.yolt.providers.common.cryptography;

import lombok.NoArgsConstructor;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static lombok.AccessLevel.PRIVATE;

/**
 * Small class that contains utility hashing methods.
 */
@NoArgsConstructor(access = PRIVATE)
public class HashUtils {

    public static String sha256Hash(final String string) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] digest = messageDigest.digest(string.getBytes(StandardCharsets.UTF_8));
            return new String(Hex.encode(digest));
        } catch (NoSuchAlgorithmException e) {
            throw new SecurityException(e);
        }
    }
}
