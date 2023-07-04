package com.yolt.providers.common.domain;

import lombok.*;
import org.springframework.lang.Nullable;

@Builder
@Getter
// Default values are only added when using the Builder, constructor should remain private
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class ProviderMetaData {

    /**
     * The maximum number of redirectUrls that can use the same set of Authentication Means,
     * null in case there is no maximum.
     */
    @Builder.Default
    @Nullable
    public Integer maximumRedirectUrlsPerAuthenticationMeans = null;

    /**
     * The internal documentation (README) associated with given provider.
     * Content of documentation is encoded.
     */
    @Builder.Default
    @Nullable
    public ProviderInternalDocumentation internalDocumentation = null;

}
