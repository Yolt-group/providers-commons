package com.yolt.providers.common.providerdetail;

import com.yolt.providers.common.providerdetail.dto.AisSiteDetails;

import java.util.List;

public interface AisDetailsProvider {

    List<AisSiteDetails> getAisSiteDetails();
}
