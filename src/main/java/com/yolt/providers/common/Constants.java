package com.yolt.providers.common;

import java.util.UUID;

// TODO: Verify if still used and delete with C4PO-8019. Providers should be ready for any client not just some hardcoded list.
@Deprecated
public class Constants {

    private Constants(){}

    public static final UUID CLIENT_ID_YOLT = UUID.fromString("297ecda4-fd60-4999-8575-b25ad23b249c");
    public static final UUID CLIENT_REDIRECT_URL_ID_YOLT_APP = UUID.fromString("cee03d67-664c-45d1-b84d-eb042d88ce65");

    /**
     * The values below belong to a 'testclient'. It is not used by any external party. It is used primarly in cucumber tests.
     * It is inserted via the testscripts so it is available on every environment (inc. team-envs) so the cucumber tests can be run
     * everywhere. (and you can also use this for any manual test)
     */
    public static final UUID CLIENT_ID_TEST_CLIENT = UUID.fromString("a2034b12-7dcc-11e8-adc0-fa7ae01bbebc");

    public static final UUID CLIENT_APPLICATION_ID_TEST_CLIENT = UUID.fromString("29e085b0-7dd0-11e8-adc0-fa7ae01bbebc");
    public static final UUID CLIENT_APPLICATION_ID_TEST_CLIENT_ID_2 = UUID.fromString("4c58a5cc-7de0-11e8-adc0-fa7ae01bbebc");

    public static final UUID CLIENT_ID_ING_FRANCE = UUID.fromString("a5154eb9-9f47-43b4-81b1-fce67813c002");

    /**
     * This is not used yet. ING France does not need to use it because they only rely on scraping. However, once they
     * do implement the redirection scheme, they do need this.
     * The reason why we're also adding this, is so we can at least test this in functional tests (cucumber).
     * There should be some 'redirectUrl' in the database, other than yolt, that can connect through client-proxy and use the
     * url-add-bank.
     */
    public static final UUID CLIENT_REDIRECT_URL_ID_ING_FRANCE = UUID.fromString("82a19af9-dd8f-4d26-92e4-38e71f4aef67");

    public static final UUID CLIENT_ID_CASY_ISR = UUID.fromString("91a91388-d505-46bf-bf6c-c0ffa920c2d0");
    public static final UUID CLIENT_ID_CASY_ISR_REDIRECT_URL_ID = UUID.fromString("36e2d4e6-7f67-11e8-adc0-fa7ae01bbebc");

    public static final UUID CLIENT_ID_CASY_ING_PL = UUID.fromString("c18df055-d2ea-460c-9252-f6bc0eddca15");
    public static final UUID CLIENT_ID_CASY_ING_PL_REDIRECT_URL_ID = UUID.fromString("5f0c6af2-9945-4e6a-97d5-ac519f74fd84");


    public static final UUID CLIENT_ID_CASY_ING_BE = UUID.fromString("9fd1c25c-3bc9-4af7-8d51-424440bcdfb4");
    public static final UUID CLIENT_ID_CASY_ING_BE_REDIRECT_URL_ID_1 = UUID.fromString("337f5dc5-6073-4a39-904a-b9e43e9691ba");
    public static final UUID CLIENT_ID_CASY_ING_BE_REDIRECT_URL_ID_2 = UUID.fromString("94e9b8bd-52f8-404d-89c0-c5e0c78dbe1a");

    public static final UUID CLIENT_ID_CASY_ING_NL = UUID.fromString("44b561bf-bf83-4ddb-ac54-59e52b75081f");
    public static final UUID CLIENT_ID_CASY_ING_NL_REDIRECT_URL_ID_1 = UUID.fromString("5c52bc9f-5fb5-4abd-a0b5-b82a480a6146");
    public static final UUID CLIENT_ID_CASY_ING_NL_REDIRECT_URL_ID_2 = UUID.fromString("7e68c72f-a07e-4f49-936e-bacf256f5eec");

    public static final UUID CLIENT_ID_PERFORMANCE_YOLT = UUID.fromString("5aeafb70-6e5e-4cf5-a13e-3023846fc928");
    public static final UUID CLIENT_ID_PERFORMANCE_YOLT_REDIRECT_URL_ID = UUID.fromString("d7b577b8-02f4-4351-9d1d-f39b773ed9f7");
}
