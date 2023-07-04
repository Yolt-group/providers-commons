package com.yolt.providers.common.exception;

/**
 * Does count towards opening circuit, as it's not user specific.
 */
public class MissingAuthenticationMeansException extends RuntimeException {

    public MissingAuthenticationMeansException(final String provider, final String authenticationKey) {
        super("Missing authentication mean for " + provider + ", authenticationKey=" + authenticationKey);
    }
}
