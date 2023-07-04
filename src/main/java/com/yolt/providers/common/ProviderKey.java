package com.yolt.providers.common;

import com.yolt.providers.common.providerinterface.Provider;
import lombok.Getter;

/**
 * @deprecated It is no longer necessary to add a providerKey.
 * You can use {@link Provider#getProviderIdentifier()} and {@link Provider#getProviderIdentifierDisplayName()} to identify and name
 * a provider. It is no longer necessary to change provider-commons for that.
 */
@Deprecated
@Getter
public enum ProviderKey {

    // Scraping:
    YODLEE("Yodlee"),
    BUDGET_INSIGHT("Budget Insight"),
    SALTEDGE("Salt Edge"),

    // Berlin group:
    RABOBANK("Rabobank"),
    UNICREDIT("Unicredit"),
    POSTE_ITALIANE("Poste Italiane"),
    UBI_BANCA("UBI Banca"),
    SNS_BANK("SNS Bank"),
    REGIO_BANK("Regio Bank"),
    INTESA_SANPAOLO("Intesa Sanpaolo"),
    
    // BPCE Group
    BANQUE_POPULAIRE("Banque Populaire"),
    CAISSE_DEPARGNE_PARTICULIERS("Caisse d'epargne particuliers"),

    // Openbanking:
    AIB("AIB"),
    BANK_OF_CYPRUS("Bank of Cyprus"),
    BANK_OF_SCOTLAND("Bank of Scotland"),
    BANK_OF_IRELAND("Bank of Ireland"),
    BARCLAYS("Barclays"),
    BARCLAYS_CARD("Barclays Credit Card"),
    DANSKEBANK("Danske Bank"),
    FIRST_TRUST("First Trust"),
    HALIFAX("Halifax"),
    HSBC("HSBC"),
    FIRST_DIRECT("First Direct"),
    MARKS_AND_SPENCER("Marks and Spencer"),
    LLOYDS_BANK("Lloyds Bank"),
    NATIONWIDE("Nationwide"),
    NATWEST("Natwest"),
    NSANDI("NS&I"),
    ROYAL_BANK_OF_SCOTLAND("Royal Bank of Scotland"),
    SANTANDER("Santander"),
    TEST_IMPL_OPENBANKING("Test Impl OpenBanking"),
    YOLT_PROVIDER("Yolt Provider"),
    ULSTER_BANK("Ulster Bank"),
    REVOLUT("Revolut"),
    MONZO_PH_OB("Monzo"),
    TSB_BANK("TSB"),
    TSB_BANK_CORPO("TSB - corporate accounts"),
    TESCO_BANK("Tesco Bank"),
    VIRGIN_MONEY("Virgin Money"),
    YORKSHIRE_BANK("Yorkshire Bank"),
    AQUA_CREDIT_CARD("Aqua Credit Card"),
    AMAZON_CREDIT_CARD("Amazon Credit Card"),
    MBNA_CREDIT_CARD("MBNA Credit Card"),
    CLYDESDALE_BANK("Clydesdale Bank"),
    SAINSBURYS_BANK("Sainsbury's Bank"),
    VANQUIS_BANK("Vanquis Bank (UK)"),
    COUTTS("Coutts"),

    // Openbanking - corporate:
    NATWEST_CORPO("Natwest - corporate accounts"),
    ROYAL_BANK_OF_SCOTLAND_CORPO("Royal Bank of Scotland - corporate accounts"),
    BANK_OF_SCOTLAND_CORPO("Bank of Scotland - corporate accounts"),
    LLOYDS_BANK_CORPO("Lloyds Bank - corporate accounts"),
    HSBC_CORPO("HSBC - corporate accounts"),
    MONZO_CORPO("Monzo - corporate accounts"),

    // SEPA PIS
    YOLT_PROVIDER_SEPA_PIS("Yolt Provider SEPA PIS"),

    //PolishAPI:
    POLISH_API_MOCK("Polish API Mock"),
    PKO_BANK_POLSKI("PKO Bank Polski"),

    // Others:
    BBVA("BBVA"),
    CESKA("Česká spořitelna"),
    CSOB("ČSOB"),
    METRO("Metro"),
    MONZO("Monzo"),
    STARLINGBANK("Starling Bank"),
    BUNQ("BUNQ"),
    ING_NL("ING NL"),
    ING_FR("ING FR"),
    CHIP("Chip"),
    ABN_AMRO("ABN AMRO"),
    RAISIN("Raisin"),
    BNP_PARIBAS("BNP Paribas"),
    HELLO_BANK("Hello bank!"),
    CREDITAGRICOLE("Crédit Agricole"),
    CREDIT_MUTUEL("Credit Mutuel"),
    LA_BANQUE_POSTALE("La Banque Postale"),
    SOCIETE_GENERALE_PRI("Societe Generale Particuliers"),
    SOCIETE_GENERALE_PRO("Societe Generale Professionnels"),
    SOCIETE_GENERALE_ENT("Societe Generale Entreprises"),

    // Partners
    PENSIONBEE("PensionBee"),
    WEALTHIFY("Wealthify");

    /**
     * The display name of a provider which is used to show it to the frontend in YAP for example.
     */
    private final String displayName;

    ProviderKey(String displayName) {
        this.displayName = displayName;
    }
}