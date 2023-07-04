package com.yolt.providers.common.exception;

/**
 * Does count towards opening circuit, as it's not user specific.
 */
public class FlowNotSupportedException extends RuntimeException {

    public FlowNotSupportedException(final String provider, final String flow) {
        super("Flow not supported for " + provider + ", flow=" + flow
        );
    }
}
