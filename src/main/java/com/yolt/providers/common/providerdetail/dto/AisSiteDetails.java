package com.yolt.providers.common.providerdetail.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import nl.ing.lovebird.providerdomain.AccountType;
import nl.ing.lovebird.providerdomain.ServiceType;

import java.util.*;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class AisSiteDetails {

    public static AisSiteDetailsBuilder site(@NonNull String id, @NonNull String name, @NonNull String providerKey, @NonNull ProviderType providerType, @NonNull List<ProviderBehaviour> providerBehaviours, @NonNull List<AccountType> accountTypeWhitelist, @NonNull List<CountryCode> availableInCountries) {
        return AisSiteDetails.builder()
                .id(UUID.fromString(id))
                .name(name)
                .providerKey(providerKey)
                .providerType(providerType)
                .providerBehaviours(providerBehaviours)
                .accountTypeWhiteList(accountTypeWhitelist)
                .availableCountries(availableInCountries);
    }

    /**
     * Name of the site, this is shown to an end-user and is thus language dependent.
     */
    String name;
    /**
     * Identifier under which records are stored in DB
     */
    String providerKey;
    ProviderType providerType;
    List<ProviderBehaviour> providerBehaviours;
    /**
     * Used to indicate that several sites form a "group", more about this in /docs/concepts/site.md
     */
    String groupingBy;
    /**
     * Unique identifier of a site.  Do **NOT** ever change this.  Generate a new one using for instance `uuidgen`.
     */
    UUID id;
    /**
     * What account types are supported for this site via the provider?
     */
    List<AccountType> accountTypeWhiteList;
    /**
     * In what countries is the site available?
     */
    List<CountryCode> availableCountries;
    /**
     * Once a user gives us consent to view their data, for how long does that consent remain valid?
     * <p>
     * Note: values for this are all over the place, don't trust it.
     */
    @Builder.Default
    Integer consentExpiryInDays = 90;
    /**
     * Is this a test site yes/no?
     */
    @JsonProperty(value = "isTestSite")
    boolean isTestSite;
    /**
     * Does the bank only permit a user to give consent to a single account per user site?
     */
    @Builder.Default
    Set<ConsentBehavior> consentBehavior = Collections.emptySet();
    /**
     * Only relevant for scraping sites.  Identifies the bank with which the scraping party must connect.
     */
    String externalId;
    /**
     * Indicates what type of steps are necessary *per* {@link ServiceType}.
     */
    @Deprecated //It is here for backward compatibility with sm use loginRequirements instead
            //TODO Remove C4PO-8806
    Map<ServiceType, List<LoginRequirement>> usesStepTypes;

    List<LoginRequirement> loginRequirements;
}
