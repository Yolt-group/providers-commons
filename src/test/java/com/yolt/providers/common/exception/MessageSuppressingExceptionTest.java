package com.yolt.providers.common.exception;

import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageSuppressingExceptionTest {

    @Test
    public void shouldUnsuppressedGetCause() {
        // given
        try { // Catch a regular exception possibly containing a sensitive message.
            try { // Try block that can be found in production code
                BigDecimal.ONE.divide(BigDecimal.ZERO);
            } catch (ArithmeticException e) {
                throw new Exception(e);
            }
        } catch (Exception e) {
            final StringWriter stringWriter = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(stringWriter);

            // when
            e.printStackTrace(printWriter);

            // then
            printWriter.flush();

            final String stackTrace = stringWriter.toString();

            assertThat(stackTrace).contains("java.lang.ArithmeticException: Division by zero");
        }
    }

    @Test
    public void shouldSuppressedGetCause() {
        // given
        try { // Catch our exception under test.
            try { // Try block that can be found in production code
                BigDecimal.ONE.divide(BigDecimal.ZERO);
            } catch (ArithmeticException e) {
                throw new MessageSuppressingException(e);
            }
        } catch (MessageSuppressingException mse) {
            final StringWriter stringWriter = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(stringWriter);

            // when
            mse.printStackTrace(printWriter);

            // then
            printWriter.flush();

            final String stackTrace = stringWriter.toString();

            assertThat(stackTrace).doesNotContain("java.lang.ArithmeticException: Division by zero")
                    .contains("java.lang.ArithmeticException");
        }
    }
}
