package com.yolt.providers.common.cryptography;

public interface JwsSigningResult {
    String getCompactSerialization();
    String getDetachedContentCompactSerialization();
}
