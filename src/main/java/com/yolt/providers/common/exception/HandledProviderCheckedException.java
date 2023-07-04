package com.yolt.providers.common.exception;

import lombok.EqualsAndHashCode;

/**
 * Super class of *any checked exception* coming from the dataprovider that will be interpreted by the 'provider-service', and converted
 * into some 'providerRequestDTO.status'
 */
@EqualsAndHashCode
public class HandledProviderCheckedException extends Exception {

    HandledProviderCheckedException() {
    }

    HandledProviderCheckedException(Throwable cause) {
        super(cause);
    }

    HandledProviderCheckedException(String message) {
        super(message);
    }

}
