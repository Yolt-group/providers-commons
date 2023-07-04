package com.yolt.providers.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class GetAccountsDataException extends Exception {

    public GetAccountsDataException(final String message) {
        super(message);
    }
}
