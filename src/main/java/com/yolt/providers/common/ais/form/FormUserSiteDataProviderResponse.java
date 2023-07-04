package com.yolt.providers.common.ais.form;

import com.yolt.providers.common.ais.DataProviderResponse;
import lombok.Data;
import nl.ing.lovebird.providerdomain.ProviderAccountDTO;
import nl.ing.lovebird.providerdomain.ProviderAccountNumberDTO;

import java.util.Collections;
import java.util.List;

@Data
public class FormUserSiteDataProviderResponse extends DataProviderResponse {

    /**
     * For form providers we need an additional status.
     * At API providers there are 2 possibilities:
     *  - exception
     *  - data
     *
     *  Here, there are more options:
     *  - exception
     *  - data
     *    - now
     *    - postponed, should come in through callback.
     *
     *    We need to let the generic code know that everything went fine so far, and that we expect a callback now.
     */
    private Status status;

    public FormUserSiteDataProviderResponse(List<ProviderAccountDTO> accounts, Status status) {
        super(accounts);
        this.status = status;
    }

    public static FormUserSiteDataProviderResponse getEmptyInstance(Status status) {
        return new FormUserSiteDataProviderResponse(Collections.emptyList(), status);
    }

    public enum Status {
        REFRESH_IN_PROGRESS_WAITING_CALLBACK, // will result in REFRESH_IN_PROGRESS
        FINISHED,                             // will result in REFRESH_FINISHED
    }

    @Override
    public boolean isDataExpectedAsynchronousViaCallback() {
        return Status.REFRESH_IN_PROGRESS_WAITING_CALLBACK.equals(this.status);
    }

}
