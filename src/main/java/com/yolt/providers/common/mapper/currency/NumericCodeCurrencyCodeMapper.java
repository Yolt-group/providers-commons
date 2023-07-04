package com.yolt.providers.common.mapper.currency;

import nl.ing.lovebird.extendeddata.common.CurrencyCode;

import java.util.Arrays;
import java.util.Currency;

public class NumericCodeCurrencyCodeMapper implements CurrencyCodeMapper {

    public NumericCodeCurrencyCodeMapper() {
        Arrays.stream(CurrencyCode.values()).forEach(internalCurrencyCode -> {
            Currency currency = Currency.getInstance(internalCurrencyCode.name());
            currencyCodeMap.put(currency.getNumericCodeAsString(), internalCurrencyCode);
        });
    }
}
