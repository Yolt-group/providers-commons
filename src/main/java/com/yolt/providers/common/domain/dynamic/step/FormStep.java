package com.yolt.providers.common.domain.dynamic.step;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yolt.providers.common.ais.form.EncryptionDetails;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import nl.ing.lovebird.providershared.form.Form;
import org.springframework.lang.Nullable;

import java.time.Instant;

@Getter
@EqualsAndHashCode(callSuper = true)
public class FormStep extends Step {
    @NonNull
    private final Form form;

    @NonNull
    private final EncryptionDetails encryptionDetails;

    /**
     * XXX This field is added for future use so it will be easier to migrate Fromproviders to the dynamic flow in the future.
     */
    @Nullable
    private final Instant timeoutTime;

    @JsonCreator
    public FormStep(
            @NonNull @JsonProperty("form") Form form,
            @NonNull @JsonProperty("encryptionDetails") EncryptionDetails encryptionDetails,
            @Nullable @JsonProperty("timeoutTime") Instant timeoutTime, @JsonProperty("providerState") String providerState) {
        super(providerState);
        this.form = form;
        this.encryptionDetails = encryptionDetails;
        this.timeoutTime = timeoutTime;
    }
}
