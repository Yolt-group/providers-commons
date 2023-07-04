package com.yolt.providers.common.providerinterface.dto.callback;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = UserDataCallbackResponse.class, name = "UserDataCallbackResponse"),
})
public class CallbackResponseDTO {
    private String provider;
}
