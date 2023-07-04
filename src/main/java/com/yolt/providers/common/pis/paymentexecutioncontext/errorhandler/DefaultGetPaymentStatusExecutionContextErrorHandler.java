package com.yolt.providers.common.pis.paymentexecutioncontext.errorhandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yolt.providers.common.pis.paymentexecutioncontext.exception.ResponseBodyValidationException;
import com.yolt.providers.common.pis.paymentexecutioncontext.model.EnhancedPaymentStatus;
import com.yolt.providers.common.pis.paymentexecutioncontext.model.PaymentExecutionResult;
import com.yolt.providers.common.pis.paymentexecutioncontext.model.PaymentStatuses;
import com.yolt.providers.common.pis.paymentexecutioncontext.model.RawBankPaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.HttpStatusCodeException;

import java.time.Clock;
import java.time.Instant;

import static com.yolt.providers.common.pis.paymentexecutioncontext.errorhandler.PaymentExecutionContextLoggingUtil.logNonHttpStatusException;

@RequiredArgsConstructor
public class DefaultGetPaymentStatusExecutionContextErrorHandler implements PaymentExecutionContextErrorHandler {

    private final RawBankPaymentStatusMapper rawBankPaymentStatusMapper;
    private final ObjectMapper objectMapper;
    private final Clock clock;

    @SneakyThrows
    @Override
    public <HttpRequestBody, HttpResponseBody, PreExecutionResult> PaymentExecutionResult<HttpResponseBody, PreExecutionResult> handleError(HttpEntity<HttpRequestBody> httpEntity, Instant requestTimestamp, Exception ex) {
        Instant responseTimestamp = Instant.now(clock);
        String rawResponseBody = "";
        String rawResponseHeaders = "";
        RawBankPaymentStatus rawBankPaymentStatus = RawBankPaymentStatus.unknown();

        if (ex instanceof HttpStatusCodeException || ex.getCause() instanceof HttpStatusCodeException) {
            HttpStatusCodeException httpStatusCodeException = ex instanceof HttpStatusCodeException ? (HttpStatusCodeException) ex : ((HttpStatusCodeException) ex.getCause());
            rawResponseBody = httpStatusCodeException.getResponseBodyAsString();
            rawResponseHeaders = httpStatusCodeException.getResponseHeaders() != null ? httpStatusCodeException.getResponseHeaders().toString() : "";
            rawBankPaymentStatus = rawBankPaymentStatusMapper.evaluate(rawResponseBody);
        } else if (ex instanceof ResponseBodyValidationException) {
            JsonNode rawBody = ((ResponseBodyValidationException) ex).getRawResponseBody();
            rawResponseBody = objectMapper.writeValueAsString(rawBody);
            rawBankPaymentStatus = rawBankPaymentStatusMapper.evaluate(rawResponseBody);
            logNonHttpStatusException(ex);
        } else {
            logNonHttpStatusException(ex);
        }

        return new PaymentExecutionResult<>(
                requestTimestamp,
                responseTimestamp,
                objectMapper.writeValueAsString(httpEntity.getBody()),
                rawResponseBody,
                httpEntity.getHeaders().toString(),
                rawResponseHeaders,
                new PaymentStatuses(rawBankPaymentStatus, EnhancedPaymentStatus.UNKNOWN),
                null,
                null
        );
    }
}
