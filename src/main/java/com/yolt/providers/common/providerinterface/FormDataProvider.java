package com.yolt.providers.common.providerinterface;

import com.yolt.providers.common.ais.DataProviderResponse;
import com.yolt.providers.common.ais.form.*;
import com.yolt.providers.common.exception.AccessMeansExpiredException;
import com.yolt.providers.common.exception.ExternalUserSiteDoesNotExistException;
import com.yolt.providers.common.exception.HandledProviderCheckedException;
import com.yolt.providers.common.exception.ProviderFetchDataException;
import nl.ing.lovebird.providerdomain.ServiceType;
import nl.ing.lovebird.providershared.AccessMeansDTO;

import java.io.IOException;

public interface FormDataProvider extends Provider {

    default ServiceType getServiceType() {
        return ServiceType.AIS;
    }

    DataProviderResponse fetchData(FormFetchDataRequest formFetchDataRequest) throws ProviderFetchDataException, HandledProviderCheckedException;

    DataProviderResponse updateExternalUserSite(final FormUpdateExternalUserSiteRequest formUpdateExternalUserSite)
            throws ProviderFetchDataException, HandledProviderCheckedException;

    DataProviderResponse createNewExternalUserSite(final FormCreateNewExternalUserSiteRequest formCreateNewExternalUserSite) throws ProviderFetchDataException, HandledProviderCheckedException;

    DataProviderResponse submitMfa(final FormSubmitMfaRequest formSubmitMfaRequest) throws ProviderFetchDataException, HandledProviderCheckedException;

    /**
     * This method should be 'idem-potent'. In other words: when it is called multiple times, it should always return the same.
     * That also means it should not throw an exception when the user is already deleted.
     * <p>
     * If a runtime exception is thrown, that will result in a retry.
     *
     * @throws AccessMeansExpiredException
     */
    void deleteUserSite(final FormDeleteUserSiteRequest formDeleteUserSite) throws AccessMeansExpiredException;

    /**
     * This method should be 'idem-potent'. In other words: when it is called multiple times, it should always return the same.
     * That also means it should not throw an exception when the user is already deleted.
     * <p>
     * If a runtime exception is thrown, that will result in a retry.
     *
     * @throws AccessMeansExpiredException
     */
    void deleteUser(final FormDeleteUserRequest formDeleteUserRequest) throws AccessMeansExpiredException;

    LoginFormResponse fetchLoginForm(final FormFetchLoginFormRequest formFetchLoginFormRequest) throws IOException;

    DataProviderResponse triggerRefreshAndFetchData(final FormTriggerRefreshAndFetchDataRequest formTriggerRefreshAndFetchDataRequest) throws ProviderFetchDataException, HandledProviderCheckedException;

    EncryptionDetails getEncryptionDetails(final AuthenticationDetails authenticationDetails);

    FormCreateNewUserResponse createNewUser(final FormCreateNewUserRequest formCreateNewUserRequest);

    /**
     * This method is called for a user refresh. This will update the usersite status to
     * LOGIN_FAILED, so a user has to refresh the accessmeans manually.
     */
    AccessMeansDTO refreshAccessMeans(final FormRefreshAccessMeansRequest refreshAccessMeans) throws ExternalUserSiteDoesNotExistException;

    void fetchExternalUserIdsFromProvider(final FormFetchExternalUserIdsFromProviderRequest formFetchExternalUserIdsFromProviderRequest);

    void deleteExternalUserByIdAtProvider(final FormDeleteExternalUserByIdRequest formDeleteExternalUserByIdRequest);
}
