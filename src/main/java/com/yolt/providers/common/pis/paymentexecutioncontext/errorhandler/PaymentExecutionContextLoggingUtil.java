package com.yolt.providers.common.pis.paymentexecutioncontext.errorhandler;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static com.yolt.providers.common.rest.logging.RawDataType.MRDD;
import static net.logstash.logback.marker.Markers.append;

@Slf4j
class PaymentExecutionContextLoggingUtil {

    static void logNonHttpStatusException(Throwable mse) {
        Marker responseMarker = append("raw-data", "true")
                .and(append("raw-data-type", MRDD));
        try {
            log.warn("Unexpected error occurred during payment. Check RDD for full stacktrace");
            log.debug(responseMarker, " StackTrace: " + convertStackTraceToString(mse));
        } catch (IOException e) {
            log.debug(responseMarker, "Cannot create StackTrace");
        }
    }

    static String convertStackTraceToString(Throwable mse) throws IOException {
        try (
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter)) {

            mse.printStackTrace(printWriter);
            printWriter.flush();

            return stringWriter.toString();
        }
    }
}
