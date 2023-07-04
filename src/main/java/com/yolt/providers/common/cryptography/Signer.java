package com.yolt.providers.common.cryptography;

import com.yolt.securityutils.signing.SignatureAlgorithm;
import org.jose4j.jws.JsonWebSignature;

import java.util.UUID;

/**
 * This interface is used for passing a jws signer function into the provider specific code.
 * This should be used when we want to sign something ourselves, most notably when we do not have access to a private signing key.
 */
public interface Signer {

    JwsSigningResult sign(JsonWebSignature jws, UUID privateKid, SignatureAlgorithm signatureAlgorithm);

    /**
     * Calculate signature for input data using signing algorithm.
     * @param bytesToSign input data
     * @param privateKid private key id in HSM
     * @param signatureAlgorithm signature algorithm
     * @return BASE64 encoded signature
     */
    String sign(byte[] bytesToSign, UUID privateKid, SignatureAlgorithm signatureAlgorithm);
}
