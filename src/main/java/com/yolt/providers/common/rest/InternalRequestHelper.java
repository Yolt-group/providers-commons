package com.yolt.providers.common.rest;

/**
 * This class has been copied from lovebirdcommons rest module to remove coupling of providers commons project to lbc
 */
public class InternalRequestHelper {

    private static final String INTERNAL_DOMAIN_SUFFIX = ".cluster.local";

    static boolean isInternalRequestAndNotStubs(final String host) {
        // It's an internal request if ends up with our internal domain suffix or it's a service name, no dots (e.g. 'kyc')
        // The exception is stubs, which should be treated as an external service
        return (host.endsWith(INTERNAL_DOMAIN_SUFFIX) && !host.contains("stubs")) ||
                (!host.contains(".") && !host.equalsIgnoreCase("stubs"));
    }

    static boolean isInternalRequest(final String host) {
        // It's an internal request if ends up with our internal domain suffix or it's a service name, no dots (e.g. 'kyc')
        return host.endsWith(INTERNAL_DOMAIN_SUFFIX) || !host.contains(".");
    }
}
