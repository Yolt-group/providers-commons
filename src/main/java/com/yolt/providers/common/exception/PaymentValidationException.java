package com.yolt.providers.common.exception;

import com.yolt.providers.common.exception.dto.DetailedErrorInformation;
import lombok.Getter;

public class PaymentValidationException extends RuntimeException {

    @Getter
    private final DetailedErrorInformation info;

    public PaymentValidationException(DetailedErrorInformation info) {
        super("Field " + info.getFieldName() + " should match pattern: " + info.getPattern());
        this.info = info;
    }
}
