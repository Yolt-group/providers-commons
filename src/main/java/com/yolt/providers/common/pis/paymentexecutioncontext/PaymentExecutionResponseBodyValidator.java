package com.yolt.providers.common.pis.paymentexecutioncontext;

import com.fasterxml.jackson.databind.JsonNode;
import com.yolt.providers.common.pis.paymentexecutioncontext.exception.ResponseBodyValidationException;

public interface PaymentExecutionResponseBodyValidator<HttpResponseBody> {

    void validate(HttpResponseBody httpResponseBody, JsonNode rawResponseBody) throws ResponseBodyValidationException;
}
