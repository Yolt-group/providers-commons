package com.yolt.providers.common.pis.paymentexecutioncontext.exception;

/**
 * Special kind of exception used widely across Payment Execution Context.
 * It is the only exception that can be thrown in any place within entire payment flow.
 */
public class PaymentExecutionTechnicalException extends RuntimeException {

    private static final String REQUEST_CREATION_ERROR_CODE = "request_creation_error";
    private static final String SUBMIT_PREPARATION_ERROR_CODE = "submit_preparation_error";
    private static final String STATUS_NOT_SUPPORTED_ERROR_CODE = "status_not_supported";
    private static final String STATUS_FAILED_ERROR_CODE = "status_failed";

    private PaymentExecutionTechnicalException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Factory method that must be used across payment initiation flow
     * @param cause
     * @return
     */
    public static PaymentExecutionTechnicalException paymentInitiationException(Throwable cause) {
        return new PaymentExecutionTechnicalException(REQUEST_CREATION_ERROR_CODE, cause);
    }

    /**
     * Factory method that must be used across payment submission flow
     * @param cause
     * @return
     */
    public static PaymentExecutionTechnicalException paymentSubmissionException(Throwable cause) {
        return new PaymentExecutionTechnicalException(SUBMIT_PREPARATION_ERROR_CODE, cause);
    }

    /**
     * Factory method that must be used when bank DOES NOT SUPPORT status endpoint.
     * By throwing this kind of exception, s-m will be notified not to call
     * providers payment status endpoint anymore (preventing infinite invocation cycle)
     * @param throwable
     * @return
     */
    public static PaymentExecutionTechnicalException statusNotSupported(Throwable throwable) {
        return new PaymentExecutionTechnicalException(STATUS_NOT_SUPPORTED_ERROR_CODE, throwable);
    }

    /**
     * Factory method that must be used when bank SUPPORTS status endpoint, but endpoint didn't work.
     * In this case s-m will try to call providers status endpoint later.
     * @param throwable
     * @return
     */
    public static PaymentExecutionTechnicalException statusFailed(Throwable throwable) {
        return new PaymentExecutionTechnicalException(STATUS_FAILED_ERROR_CODE, throwable);
    }
}
