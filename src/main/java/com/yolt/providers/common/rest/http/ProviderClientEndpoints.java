package com.yolt.providers.common.rest.http;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProviderClientEndpoints {

    // OAuth2.0 stuff
    public static final String CLIENT_CREDENTIALS_GRANT = "client_credentials_grant";
    public static final String GET_ACCESS_TOKEN = "get_access_token";

    // Dynamic Registration
    public static final String REGISTER = "register";

    // AIS stuff
    public static final String GET_ACCOUNT_REQUEST_ID = "get_account_request_id";
    public static final String RETRIEVE_ACCOUNT_REQUEST_ID = "retrieve_account_request_id";
    public static final String GET_ACCOUNT_ACCESS_CONSENT = "get_account_access_consent";
    public static final String RETRIEVE_ACCOUNT_ACCESS_CONSENT = "retrieve_account_access_consent";
    public static final String DELETE_ACCOUNT_REQUEST = "delete_account_request";
    public static final String DELETE_ACCOUNT_ACCESS_CONSENT = "delete_account_access_consent";
    public static final String GET_ACCOUNTS = "get_accounts";
    public static final String GET_TRANSACTIONS_BY_ACCOUNT_ID = "get_transactions_by_account_id";
    public static final String GET_BALANCES_BY_ACCOUNT_ID = "get_balances_by_account_id";
    public static final String GET_STANDING_ORDERS_BY_ACCOUNT_ID = "get_standing_orders_by_account_id";
    public static final String GET_DIRECT_DEBITS_BY_ACCOUNT_ID = "get_direct_debits_by_account_id";
    public static final String REFRESH_TOKEN = "refresh_token";

    // PIS stuff
    public static final String INITIATE_PAYMENT = "initiate_payment";
    public static final String SUBMIT_PAYMENT = "submit_payment";
    public static final String GET_PAYMENT_STATUS = "get_payment_status";
}
