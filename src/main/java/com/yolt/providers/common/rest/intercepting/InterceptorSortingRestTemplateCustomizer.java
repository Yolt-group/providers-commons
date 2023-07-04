package com.yolt.providers.common.rest.intercepting;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.OrderComparator;
import org.springframework.core.Ordered;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Purpose of this class is sorting ClientHttpRequestInterceptor instances
 * provided to RestTemplate based on Order value taken from Ordered interface
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InterceptorSortingRestTemplateCustomizer {

    public static RestTemplate customize(final RestTemplate restTemplate) {
        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        if (interceptors.isEmpty()) {
            return restTemplate;
        }

        List<ClientHttpRequestInterceptor> sortedInterceptors = interceptors.stream()
                .map(i -> i instanceof Ordered ? i : new DefaultOrderClientHttpRequestInterceptorWrapper(i))
                .sorted(OrderComparator.INSTANCE)
                .collect(Collectors.toList());
        restTemplate.setInterceptors(sortedInterceptors);

        return restTemplate;
    }
}
