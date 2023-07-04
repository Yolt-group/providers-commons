package com.yolt.providers.common.providerdetail.dto;

import com.yolt.providers.common.pis.common.PaymentType;
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PisSiteDetails {

    @NonNull
    private UUID id;

    @NonNull
    private String providerKey;

    @NonNull
    private boolean supported;

    @NonNull
    private PaymentType paymentType;

    @NonNull
    private Map<DynamicFieldNames, DynamicFieldOptions> dynamicFields;

    @NonNull
    private boolean requiresSubmitStep;

    @NonNull
    private PaymentMethod paymentMethod;

    @NonNull
    private List<LoginRequirement> loginRequirements;
}
