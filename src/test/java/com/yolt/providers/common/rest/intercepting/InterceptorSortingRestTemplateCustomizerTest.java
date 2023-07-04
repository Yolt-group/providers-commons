package com.yolt.providers.common.rest.intercepting;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.Ordered;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class InterceptorSortingRestTemplateCustomizerTest {

    @Test
    public void shouldSortInterceptorsByOrder() {
        // given
        ClientHttpRequestInterceptor unOrderedInterceptor = new MockUnorderedClientHttpRequestInterceptor();
        ClientHttpRequestInterceptor lastInterceptor = new MockOrderedClientHttpRequestInterceptor(1);
        ClientHttpRequestInterceptor firstInterceptor = new MockOrderedClientHttpRequestInterceptor(-1);

        List<ClientHttpRequestInterceptor> interceptors = Arrays.asList(
                unOrderedInterceptor, lastInterceptor, firstInterceptor
        );

        RestTemplate restTemplate = new RestTemplateBuilder()
                .additionalInterceptors(interceptors)
                .build();

        // when
        RestTemplate result = InterceptorSortingRestTemplateCustomizer.customize(restTemplate);

        // then
        List<ClientHttpRequestInterceptor> resultingInterceptors = result.getInterceptors();
        assertThat(resultingInterceptors).first().isEqualTo(firstInterceptor);
        assertThat(resultingInterceptors).last().isEqualTo(lastInterceptor);

        assertThat(resultingInterceptors).element(1).isInstanceOf(DefaultOrderClientHttpRequestInterceptorWrapper.class);
        assertThat(resultingInterceptors).element(1).matches(i -> ((Ordered) i).getOrder() == 0);
    }

    @Test
    public void shouldSortInterceptorsByOrderWhenInterceptorsAreReplaced() {
        // given
        ClientHttpRequestInterceptor unOrderedInterceptor = new MockUnorderedClientHttpRequestInterceptor();
        ClientHttpRequestInterceptor lastInterceptor = new MockOrderedClientHttpRequestInterceptor(1);
        ClientHttpRequestInterceptor firstInterceptor = new MockOrderedClientHttpRequestInterceptor(-1);

        List<ClientHttpRequestInterceptor> interceptors = Arrays.asList(
                unOrderedInterceptor, lastInterceptor, firstInterceptor
        );

        RestTemplate restTemplate = new RestTemplateBuilder()
                .interceptors(interceptors)
                .build();

        // when
        RestTemplate result = InterceptorSortingRestTemplateCustomizer.customize(restTemplate);

        // then
        List<ClientHttpRequestInterceptor> resultingInterceptors = result.getInterceptors();
        assertThat(resultingInterceptors).first().isEqualTo(firstInterceptor);
        assertThat(resultingInterceptors).last().isEqualTo(lastInterceptor);

        assertThat(resultingInterceptors).element(1).isInstanceOf(DefaultOrderClientHttpRequestInterceptorWrapper.class);
        assertThat(resultingInterceptors).element(1).matches(i -> ((Ordered) i).getOrder() == 0);
    }

    @Test
    public void thatPerformedSortingIsStable() {
        // given
        ClientHttpRequestInterceptor lastInterceptor = new MockOrderedClientHttpRequestInterceptor(2);
        ClientHttpRequestInterceptor sameOrderInterceptorButAddedFirstInQueue = new MockOrderedClientHttpRequestInterceptor(1);
        ClientHttpRequestInterceptor sameOrderInterceptorButAddedSecondInQueue = new MockOrderedClientHttpRequestInterceptor(1);
        ClientHttpRequestInterceptor sameOrderInterceptorButAddedThirdInQueue = new MockOrderedClientHttpRequestInterceptor(1);
        ClientHttpRequestInterceptor firstInterceptor = new MockOrderedClientHttpRequestInterceptor(-2);

        List<ClientHttpRequestInterceptor> interceptors = Arrays.asList(
                sameOrderInterceptorButAddedFirstInQueue, sameOrderInterceptorButAddedSecondInQueue, sameOrderInterceptorButAddedThirdInQueue,
                firstInterceptor, lastInterceptor
        );

        RestTemplate restTemplate = new RestTemplateBuilder()
                .additionalInterceptors(interceptors)
                .build();

        // when
        RestTemplate result = InterceptorSortingRestTemplateCustomizer.customize(restTemplate);

        // then
        List<ClientHttpRequestInterceptor> resultingInterceptors = result.getInterceptors();
        assertThat(resultingInterceptors).element(0).isEqualTo(firstInterceptor);
        assertThat(resultingInterceptors).element(1).isEqualTo(sameOrderInterceptorButAddedFirstInQueue);
        assertThat(resultingInterceptors).element(2).isEqualTo(sameOrderInterceptorButAddedSecondInQueue);
        assertThat(resultingInterceptors).element(3).isEqualTo(sameOrderInterceptorButAddedThirdInQueue);
        assertThat(resultingInterceptors).element(4).isEqualTo(lastInterceptor);
    }

    @Test
    public void thatPerformedSortingIsStableWithUnorderedInterceptors() {
        // given
        ClientHttpRequestInterceptor defaultOrderInterceptorButAddedFirstInQueue = new MockUnorderedClientHttpRequestInterceptor();
        ClientHttpRequestInterceptor defaultOrderInterceptorButAddedThirdInQueue = new MockUnorderedClientHttpRequestInterceptor();
        ClientHttpRequestInterceptor defaultOrderInterceptorButAddedSecondInQueue = new MockUnorderedClientHttpRequestInterceptor();


        List<ClientHttpRequestInterceptor> interceptors = Arrays.asList(
                defaultOrderInterceptorButAddedFirstInQueue, defaultOrderInterceptorButAddedSecondInQueue, defaultOrderInterceptorButAddedThirdInQueue
        );

        RestTemplate restTemplate = new RestTemplateBuilder()
                .additionalInterceptors(interceptors)
                .build();

        // when
        RestTemplate result = InterceptorSortingRestTemplateCustomizer.customize(restTemplate);

        // then
        List<ClientHttpRequestInterceptor> resultingInterceptors = result.getInterceptors();
        assertThat(resultingInterceptors).first()
                .matches(i -> ((DefaultOrderClientHttpRequestInterceptorWrapper) i).getDelegate().equals(defaultOrderInterceptorButAddedFirstInQueue));
        assertThat(resultingInterceptors).element(1)
                .matches(i -> ((DefaultOrderClientHttpRequestInterceptorWrapper) i).getDelegate().equals(defaultOrderInterceptorButAddedSecondInQueue));
        assertThat(resultingInterceptors).last()
                .matches(i -> ((DefaultOrderClientHttpRequestInterceptorWrapper) i).getDelegate().equals(defaultOrderInterceptorButAddedThirdInQueue));
    }

    @Test
    public void shouldReturnWithoutChangesWhenInterceptorsAreEmpty() {
        // given
        RestTemplate restTemplate = new RestTemplateBuilder().build();

        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();

        // when
        RestTemplate result = InterceptorSortingRestTemplateCustomizer.customize(restTemplate);

        // then
        assertThat(result.getInterceptors()).isEqualTo(interceptors);
    }
}