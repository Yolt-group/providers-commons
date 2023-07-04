package com.yolt.providers.common.exception;

/**
 * Does count towards opening circuit, as it's not user specific.
 */
public class InvalidAuthenticationMeansException extends RuntimeException {

    public InvalidAuthenticationMeansException(final String provider, final String authenticationKey, final String errorMessage) {
        super("Invalid authentication mean for " + provider + ", authenticationKey=" + authenticationKey + "errorMessage=" + errorMessage);
    }
}
