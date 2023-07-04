package com.yolt.providers.common.domain.dynamic.step;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = FormStep.class, name = "FORM"),
        @JsonSubTypes.Type(value = RedirectStep.class, name = "REDIRECT_URL")

})
@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public abstract class Step {
    @Nullable
    private final String providerState;
}
