package com.yolt.providers.common.rest.logging;

import com.yolt.providers.common.domain.logging.RawDataCensoringRule;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class CensoredHeadersProducerTest {

    @Test
    public void shouldCensorHeadersAccordingToRules() {
        // given
        RawDataCensoringRule bearerRule = new RawDataCensoringRule(HttpHeaders.AUTHORIZATION, "bearer ***");

        List<RawDataCensoringRule> rules = Collections.singletonList(bearerRule);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("SENSITIVE-TOKEN");
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        CensoredHeadersProducer testSubject = new CensoredHeadersProducer(rules);

        // when
        HttpHeaders result = testSubject.getCensoredHeaders(headers);

        // then
        Assertions.assertThat(result).hasSize(2);
        Assertions.assertThat(result.getFirst(HttpHeaders.AUTHORIZATION)).isEqualTo(bearerRule.getReplacement());

    }

    @Test
    public void shouldCensorHeadersAndReturnNewCopyWithoutMakingAnyChangesToOriginalHeaders() {
        // given
        String sensitiveListHeader = "MySensitiveListHeader";

        RawDataCensoringRule bearerRule = new RawDataCensoringRule(HttpHeaders.AUTHORIZATION, "bearer ***");
        RawDataCensoringRule sensitiveListRule = new RawDataCensoringRule(sensitiveListHeader, "bearer ****");
        List<RawDataCensoringRule> rules = Arrays.asList(bearerRule, sensitiveListRule);

        HttpHeaders originalHeaders = new HttpHeaders();

        String sensitiveBearerValue = "SENSITIVE-TOKEN";
        originalHeaders.setBearerAuth("SENSITIVE-TOKEN");

        List<String> sensitiveValues = Arrays.asList("Sensitive-1", "Sensitive-2");
        originalHeaders.put(sensitiveListHeader, sensitiveValues);

        CensoredHeadersProducer testSubject = new CensoredHeadersProducer(rules);

        // when
        HttpHeaders result = testSubject.getCensoredHeaders(originalHeaders);

        // then
        Assertions.assertThat(originalHeaders).hasSize(2);
        Assertions.assertThat(originalHeaders.getFirst(HttpHeaders.AUTHORIZATION)).isEqualTo("Bearer " + sensitiveBearerValue);
        Assertions.assertThat(originalHeaders.get(sensitiveListHeader)).isEqualTo(sensitiveValues);

        // verify that resulting map[ is different copy by reference
        Assertions.assertThat(result == originalHeaders).isFalse();
        Assertions.assertThat(result).isNotEqualTo(originalHeaders);

        // verify that resulting list is different copy by reference
        Assertions.assertThat(result.get(sensitiveListHeader) == originalHeaders.get(sensitiveListHeader)).isFalse();

        Assertions.assertThat(result).hasSize(2);
        Assertions.assertThat(result.getFirst(HttpHeaders.AUTHORIZATION)).isEqualTo(bearerRule.getReplacement());
        Assertions.assertThat(result.get(sensitiveListHeader)).allMatch(v -> v.equals(sensitiveListRule.getReplacement()));
    }

    @Test
    public void shouldMatchHeadersCaseInsensitive() {
        // given
        RawDataCensoringRule bearerRule = new RawDataCensoringRule(HttpHeaders.AUTHORIZATION, "bearer ***");

        List<RawDataCensoringRule> rules = Collections.singletonList(bearerRule);

        HttpHeaders headers = new HttpHeaders();
        headers.set("AuThOriZation", "SENSITIVE-TOKEN");
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        CensoredHeadersProducer testSubject = new CensoredHeadersProducer(rules);

        // when
        HttpHeaders result = testSubject.getCensoredHeaders(headers);

        // then
        Assertions.assertThat(result).hasSize(2);
        Assertions.assertThat(result.getFirst(HttpHeaders.AUTHORIZATION)).isEqualTo(bearerRule.getReplacement());

    }

    @Test
    public void shouldLeaveHeadersUntouchedIfTheyDontMatchCriteria() {
        // given
        RawDataCensoringRule bearerRule = new RawDataCensoringRule(HttpHeaders.AUTHORIZATION, "bearer ***");

        List<RawDataCensoringRule> rules = Collections.singletonList(bearerRule);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("SENSITIVE-TOKEN");
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        CensoredHeadersProducer testSubject = new CensoredHeadersProducer(rules);

        // when
        HttpHeaders result = testSubject.getCensoredHeaders(headers);

        // then
        Assertions.assertThat(result).hasSize(2);
        Assertions.assertThat(result.getFirst(HttpHeaders.AUTHORIZATION)).isEqualTo(bearerRule.getReplacement());

    }

}