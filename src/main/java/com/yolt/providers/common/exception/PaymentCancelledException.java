package com.yolt.providers.common.exception;

public class PaymentCancelledException extends RuntimeException {
    public PaymentCancelledException(final String message){
        super(message);
    }

    public PaymentCancelledException(){
    }

}
