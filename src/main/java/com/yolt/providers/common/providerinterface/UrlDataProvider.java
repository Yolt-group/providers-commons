package com.yolt.providers.common.providerinterface;

import com.yolt.providers.common.ais.DataProviderResponse;
import com.yolt.providers.common.ais.url.*;
import com.yolt.providers.common.domain.dynamic.AccessMeansOrStepDTO;
import com.yolt.providers.common.domain.dynamic.step.Step;
import com.yolt.providers.common.exception.MissingDataException;
import com.yolt.providers.common.exception.ProviderFetchDataException;
import com.yolt.providers.common.exception.TokenInvalidException;
import nl.ing.lovebird.providerdomain.ServiceType;
import nl.ing.lovebird.providershared.AccessMeansDTO;


public interface UrlDataProvider extends Provider {

    default ServiceType getServiceType() {
        return ServiceType.AIS;
    }

    /**
     * This method fetches the account and transactions data from the provider. If something random goes wrong with
     * an account, try to throw a ProviderFetchDataException with the raw json in there for debugging purposes. If the
     * token is not valid, throw a TokenInvalidException.
     * <p>
     * If there is no exception, but also nothing to return, please do return an empty DataProviderResponse. The
     * calling logic will make the right choices in this case.
     *
     * @throws ProviderFetchDataException The exception with a list of failedAccounts, these will be written to Kafka
     *                                    for debugging purposes.
     * @throws TokenInvalidException      In case the provided token is not valid.
     */
    DataProviderResponse fetchData(UrlFetchDataRequest urlFetchData) throws ProviderFetchDataException, TokenInvalidException;

    /**
     * @throws MissingDataException when data received from bank are incomplete.
     */
    Step getLoginInfo(UrlGetLoginRequest urlGetLogin);

    /**
     * @throws MissingDataException when data received from bank are incomplete.
     */
    AccessMeansOrStepDTO createNewAccessMeans(final UrlCreateAccessMeansRequest urlCreateAccessMeans);

    /**
     * This method is called for an access token refresh.
     */
    AccessMeansDTO refreshAccessMeans(UrlRefreshAccessMeansRequest urlRefreshAccessMeans) throws TokenInvalidException;

    /**
     * This method is called whenever a user site is going to be deleted.
     */
    default void onUserSiteDelete(UrlOnUserSiteDeleteRequest urlOnUserSiteDeleteRequest) throws TokenInvalidException {
        // Do nothing
    }
}
