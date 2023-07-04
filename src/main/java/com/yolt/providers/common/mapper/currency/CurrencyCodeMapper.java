package com.yolt.providers.common.mapper.currency;

import nl.ing.lovebird.extendeddata.common.CurrencyCode;

import java.util.HashMap;
import java.util.Map;

public interface CurrencyCodeMapper {

    Map<String, CurrencyCode> currencyCodeMap = new HashMap<>();

    default CurrencyCode mapToCurrencyCode(String code) {
        return currencyCodeMap.get(code);
    }
}
