package com.yolt.providers.common.domain.authenticationmeans.keymaterial;

import lombok.Value;
import com.yolt.securityutils.signing.SignatureAlgorithm;
import java.util.List;
import java.util.Set;

@Value
public class KeyMaterialRequirements {
    private final Set<KeyAlgorithm> keyAlgorithms;
    private final Set<SignatureAlgorithm> signatureAlgorithms;
    private final List<DistinguishedNameElement> distinguishedNames;
}
