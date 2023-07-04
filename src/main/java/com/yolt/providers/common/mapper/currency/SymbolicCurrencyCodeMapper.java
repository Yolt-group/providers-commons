package com.yolt.providers.common.mapper.currency;

import nl.ing.lovebird.extendeddata.common.CurrencyCode;

import java.util.Arrays;

public class SymbolicCurrencyCodeMapper implements CurrencyCodeMapper {

    public SymbolicCurrencyCodeMapper() {
        Arrays.stream(CurrencyCode.values()).forEach(currency -> currencyCodeMap.put(currency.name(), currency));
    }
}
