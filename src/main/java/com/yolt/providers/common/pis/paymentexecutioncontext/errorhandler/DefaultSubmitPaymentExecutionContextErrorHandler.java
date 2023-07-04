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

import java.net.SocketTimeoutException;
import java.time.Clock;
import java.time.Instant;

import static com.yolt.providers.common.pis.paymentexecutioncontext.errorhandler.PaymentExecutionContextLoggingUtil.logNonHttpStatusException;

@RequiredArgsConstructor
public class DefaultSubmitPaymentExecutionContextErrorHandler implements PaymentExecutionContextErrorHandler {

    private final RawBankPaymentStatusMapper rawBankPaymentStatusMapper;
    private final ObjectMapper objectMapper;
    private final Clock clock;

    @SneakyThrows
    @Override
    public <HttpRequestBody, HttpResponseBody, PreExecutionResult> PaymentExecutionResult<HttpResponseBody, PreExecutionResult> handleError(HttpEntity<HttpRequestBody> httpEntity, Instant requestTimestamp, Exception ex) {
        Instant responseTimestamp = Instant.now(clock);
        String rawResponseBody = "";
        String rawResponseHeaders = "";
        PaymentStatuses paymentStatuses = new PaymentStatuses(RawBankPaymentStatus.unknown(), EnhancedPaymentStatus.EXECUTION_FAILED);

        if (ex instanceof HttpStatusCodeException || ex.getCause() instanceof HttpStatusCodeException) {
            HttpStatusCodeException httpStatusCodeException = ex instanceof HttpStatusCodeException ? (HttpStatusCodeException) ex : ((HttpStatusCodeException) ex.getCause());
            rawResponseBody = httpStatusCodeException.getResponseBodyAsString();
            rawResponseHeaders = httpStatusCodeException.getResponseHeaders() != null ? httpStatusCodeException.getResponseHeaders().toString() : "";
            RawBankPaymentStatus rawBankPaymentStatus = rawBankPaymentStatusMapper.evaluate(rawResponseBody);
            if (httpStatusCodeException.getStatusCode().is5xxServerError()) {
                paymentStatuses = new PaymentStatuses(rawBankPaymentStatus, EnhancedPaymentStatus.UNKNOWN);
            } else { // default if the status is 4xx
                paymentStatuses = new PaymentStatuses(rawBankPaymentStatus, EnhancedPaymentStatus.EXECUTION_FAILED);
            }
        } else if (ex instanceof ResponseBodyValidationException) {
            JsonNode rawBody = ((ResponseBodyValidationException) ex).getRawResponseBody();
            rawResponseBody = objectMapper.writeValueAsString(rawBody);
            paymentStatuses = new PaymentStatuses(rawBankPaymentStatusMapper.evaluate(rawResponseBody), EnhancedPaymentStatus.UNKNOWN);
            logNonHttpStatusException(ex);
        } else if (ex.getCause() instanceof SocketTimeoutException) {
            paymentStatuses = new PaymentStatuses(RawBankPaymentStatus.unknown(), EnhancedPaymentStatus.UNKNOWN);
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
                paymentStatuses,
                null,
                null
        );
    }
}
