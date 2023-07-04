package com.yolt.providers.common.domain.dynamic;

import com.yolt.providers.common.ais.url.UrlCreateAccessMeansRequest;
import com.yolt.providers.common.domain.dynamic.step.FormStep;
import com.yolt.providers.common.domain.dynamic.step.RedirectStep;
import com.yolt.providers.common.domain.dynamic.step.Step;
import com.yolt.providers.common.providerinterface.UrlDataProvider;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import nl.ing.lovebird.providershared.AccessMeansDTO;
import org.springframework.validation.annotation.Validated;

/**
 * This DTO is used in the dynamic login flow for (URL) dataproviders.
 * Whenever site-management calls the {@link UrlDataProvider#createNewAccessMeans(UrlCreateAccessMeansRequest)} function
 * it can either get back a fully formed {@link AccessMeansDTO} which means the logging in is successful.
 * If there are more actions required of the user providers will respond with either a {@link RedirectStep} or a {@link FormStep}.
 * This Step will contain the instructions needed to continue the creation of the AccessMeans.
 * Sitemanagement will ensure the step is executed (by the user) and it will call this endpoint with the filled in step information,
 * and the providerState related to the step.
 * This will continue until the AccessMeans can be formed. After which an {@link AccessMeansDTO} will be returned.
 */
@Value
@Validated
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AccessMeansOrStepDTO {

    private final AccessMeansDTO accessMeans;
    private final Step step;

    public AccessMeansOrStepDTO(AccessMeansDTO accessMeans) {
        this(accessMeans, null);
    }

    public AccessMeansOrStepDTO(Step step) {
        this(null, step);
    }

}