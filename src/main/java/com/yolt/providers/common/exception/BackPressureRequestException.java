package com.yolt.providers.common.exception;

/**
 * This exception should ONLY be thrown when amount of fetch data calls is limited by ASPSP (for example HTTP-429)
 * and it does not require reconsenting after exceeding the limit. It does not trigger reconsent of user in site-management.
 */
public class BackPressureRequestException extends RuntimeException {

    public BackPressureRequestException(String message) {
        super(message);
    }
}
