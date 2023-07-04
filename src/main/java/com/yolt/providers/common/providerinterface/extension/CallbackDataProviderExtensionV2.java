package com.yolt.providers.common.providerinterface.extension;

import com.yolt.providers.common.ais.form.FormCallbackProcessResponse;
import com.yolt.providers.common.ais.form.FormCallbackProcessWithMoreInfoResponse;
import com.yolt.providers.common.exception.CallbackJsonParseException;
import com.yolt.providers.common.providerinterface.dto.callback.CallbackResponseDTO;
import org.springframework.lang.NonNull;

public interface CallbackDataProviderExtensionV2 {

    /**
     *
     * Should process the body into a callbackResponseDTO.
     * Sometimes, more information is required. In that case a {@link nl.ing.lovebird.providershared.callback.MoreInfoNeededCallbackResponse}
     * can be returned. By doing so, the overloaded method will be called with more information.
     *
     * @throws CallbackJsonParseException Only throw this when the <b>entire</b> json cannot be parsed.
     */
    CallbackResponseDTO process(@NonNull final FormCallbackProcessResponse formCallbackProcessResponse) throws CallbackJsonParseException;


    /**
     *
     * This method is only called when the method {@link #process(FormCallbackProcessResponse)} returned a {@link nl.ing.lovebird.providershared.callback.MoreInfoNeededCallbackResponse}
     * This method is <b>NOT</b> allowed to return another {@link nl.ing.lovebird.providershared.callback.MoreInfoNeededCallbackResponse}
     *
     * @throws CallbackJsonParseException
     */
    CallbackResponseDTO process(@NonNull final FormCallbackProcessWithMoreInfoResponse formCallbackProcessWithMoreInfoResponse) throws CallbackJsonParseException;
}
