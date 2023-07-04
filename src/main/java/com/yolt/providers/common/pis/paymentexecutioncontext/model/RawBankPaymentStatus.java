package com.yolt.providers.common.pis.paymentexecutioncontext.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class RawBankPaymentStatus {

    @NonNull
    private final String status;

    @NonNull
    private final String reason;

    public static RawBankPaymentStatus unknown(final String reason) {
        return new RawBankPaymentStatus("UNKNOWN", reason);
    }

    public static RawBankPaymentStatus unknown() {
        return unknown("");
    }

    public static RawBankPaymentStatus forStatus(final String status, final String reason) {
        return new RawBankPaymentStatus(status, reason);
    }

    public static RawBankPaymentStatus forStatus(final String status) {
        return new RawBankPaymentStatus(status, "");
    }
}
