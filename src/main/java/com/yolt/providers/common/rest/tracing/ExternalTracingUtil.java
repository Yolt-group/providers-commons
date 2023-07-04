package com.yolt.providers.common.rest.tracing;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.MDC;

import java.util.UUID;
import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExternalTracingUtil {

    public static final String LAST_EXTERNAL_TRACE_ID = "last_external_trace_id";

    /**
     * It creates random value for last external trace ID in outgoing calls.
     * This value will also be stored in the MDC for the last_external_trace_id key.
     * @return random UUID string as external trace ID.
     */
    public static String createLastExternalTraceId() {
        String lastExternalTraceId = UUID.randomUUID().toString();
        MDC.put(LAST_EXTERNAL_TRACE_ID, lastExternalTraceId);
        return lastExternalTraceId;
    }

    /**
     * It creates value based on give suppliers for last external trace ID in outgoing calls.
     * This value will also be stored in the MDC for the last_external_trace_id key.
     * @return string based on supplier definition.
     */
    public static String createLastExternalTraceId(Supplier<String> lastExternalTraceIdSupplier) {
        String lastExternalTraceId = lastExternalTraceIdSupplier.get();
        MDC.put(LAST_EXTERNAL_TRACE_ID, lastExternalTraceId);
        return lastExternalTraceId;
    }

    /**
     * It removes last_external_trace_id key from MDC context.
     */
    public static void removeLastExternalTraceId() {
        MDC.remove(LAST_EXTERNAL_TRACE_ID);
    }
}
