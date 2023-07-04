package com.yolt.providers.common.exception.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class DetailedErrorInformation {

    private final FieldName fieldName;
    private final String pattern;

}