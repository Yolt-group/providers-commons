package com.yolt.providers.common.pis.paymentexecutioncontext.errorhandler;

import com.yolt.providers.common.pis.paymentexecutioncontext.model.RawBankPaymentStatus;

public interface RawBankPaymentStatusMapper {

    RawBankPaymentStatus mapBankPaymentStatus(final String rawBodyResponse);

    default RawBankPaymentStatus evaluate(final String rawBodyResponse) {
        try {
            return mapBankPaymentStatus(rawBodyResponse);
        } catch (Exception ex) {
            return RawBankPaymentStatus.unknown(ex.getMessage());
        }
    }
}
