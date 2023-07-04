package com.yolt.providers.common.rest.logging;

import com.yolt.providers.common.domain.logging.RawDataCensoringRule;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@EqualsAndHashCode
public class CensoredHeadersProducer {

    @NonNull
    private final Collection<RawDataCensoringRule> rules;

    public HttpHeaders getCensoredHeaders(final HttpHeaders headers) {
        if (rules.isEmpty()) {
            return headers;
        }
        HttpHeaders censoredHeaders = deepCopyHeaders(headers);
        for (RawDataCensoringRule rule : rules) {
            if (censoredHeaders.containsKey(rule.getHeaderName())) {
                List<String> values = censoredHeaders.get(rule.getHeaderName());
                if (values != null) {
                    List<String> censoredValues = values.stream()
                            .map(val -> rule.getReplacement())
                            .collect(Collectors.toList());
                    censoredHeaders.put(rule.getHeaderName(), censoredValues);
                }
            }
        }
        return censoredHeaders;
    }

    private HttpHeaders deepCopyHeaders(final HttpHeaders headers) {
        HttpHeaders copy = new HttpHeaders();
        headers.forEach((key, value) -> copy.put(key, value != null ? new ArrayList<>(value) : null));
        return copy;
    }
}
