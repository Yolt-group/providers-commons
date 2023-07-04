package com.yolt.providers.common.exception;

public class AutoOnboardingException extends RuntimeException {

    public AutoOnboardingException(final String provider, final String message, final Exception e) {
        super("Auto-onboarding failed for " + provider + ", message=" + message, e);
    }
}
