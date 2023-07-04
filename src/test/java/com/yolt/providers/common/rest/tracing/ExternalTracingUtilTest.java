package com.yolt.providers.common.rest.tracing;

import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import java.util.UUID;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

public class ExternalTracingUtilTest {

    private static final String USER_ID_HEADER_NAME = "user-id";
    private static final String USER_ID = "00000000-0000-0000-0000-000000000000";

    @Test
    public void shouldCreateExternalTraceIdByUsingDefaultGenerator() {
        // when
        String lastExternalTraceId = ExternalTracingUtil.createLastExternalTraceId();

        // then
        assertThat(MDC.get(ExternalTracingUtil.LAST_EXTERNAL_TRACE_ID)).isEqualTo(lastExternalTraceId);
    }

    @Test
    public void shouldCreateExternalTraceIdWithUsingSupplier() {
        // given
        Supplier<String> lastExternalTraceIdSupplier = () -> UUID.randomUUID().toString().replace("-", "");

        // when
        String lastExternalTraceId = ExternalTracingUtil.createLastExternalTraceId(lastExternalTraceIdSupplier);

        // then
        assertThat(lastExternalTraceId)
                .isEqualTo(MDC.get(ExternalTracingUtil.LAST_EXTERNAL_TRACE_ID))
                .doesNotContain("-");
    }

    @Test
    public void shouldCreateExternalTraceIdWithoutModifyingOtherMDCKeys() {
        // given
        MDC.put(USER_ID_HEADER_NAME, USER_ID);

        // when
        ExternalTracingUtil.createLastExternalTraceId();

        // then
        assertThat(MDC.get(USER_ID_HEADER_NAME)).isEqualTo(USER_ID);
    }

    @Test
    public void shouldRemoveExternalTraceId() {
        // given
        MDC.put(ExternalTracingUtil.LAST_EXTERNAL_TRACE_ID, UUID.randomUUID().toString());

        // when
        ExternalTracingUtil.removeLastExternalTraceId();

        // then
        assertThat(MDC.get(ExternalTracingUtil.LAST_EXTERNAL_TRACE_ID)).isNull();
    }

    @Test
    public void shouldRemoveExternalTraceIdWithoutModifyingOtherMDCKeys() {
        // given
        MDC.put(USER_ID_HEADER_NAME, USER_ID);

        // when
        ExternalTracingUtil.removeLastExternalTraceId();

        // then
        assertThat(MDC.get(USER_ID_HEADER_NAME)).isEqualTo(USER_ID);
    }
}
