package com.yolt.providers.common.providerdetail.dto;

/**
 * Please adhere to: https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2
 */
public enum CountryCode {

    GB, NL, AU, ES, AT, FR, DE, IT, CZ, RO, PL, LU, TH, BE, IE,

    /**
     * Used by SaltEdge for test banks.
     */
    XF,

    /**
     * TODO: C4PO-8279
     * UNUSED COUNTRY (Federated States of Micronesia).
     * We moved Danske Bank site here temporarily until all danske usersites are deleted by users.
     */
    FM
}
