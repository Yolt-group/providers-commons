# providers-common

This project is the ``common`` module extracted from the ``providers-parent`` project during _providers split_ POC phase. This project should not be dependent neither on ``providers-web`` nor ``polishapi`` projects.

## ClientHttpRequestInterceptor order of execution remarks
ExternalRestTemplateBuilderFactory allows defining client request interceptors on RestTemplate instances produced
by this customized RestTemplateBuilder. Due to the fact that interceptors are not always pure and may produce side effects
affecting the request, or it's execution arbitrary mechanism allowing to specify their respective order of execution is necessary.

To allow defining strict order of interceptors execution, every class implementing ClientHttpRequestInterceptor must also
implement interface org.springframework.core.Ordered and specify the value of Order for this class in respect to values defined by Other interceptors.

Interceptors added to restTemplate using RestTemplateBuilder instance in ExternalRestTemplateBuilderFactory are sorted using
class org.springframework.core.OrderComparator. Interceptors with lowest Order value are placed at the top of the list.
To make this mechanism more flexible, interceptor instances that were added to RestTemplateBuilder and don't implement the Ordered
interface are wrapped using our custom class DefaultOrderClientHttpRequestInterceptorWrapper. It's only purpose is to expose
default Order value when it's not specified.

Remember that the last interceptor in a queue will be also the first one to execute after the request therefore the first interceptor in a queue will be the last in a queue to execute after request.
Request execution chain for RestTemplate with following list of Interceptors (A, B, C) looks as follows:
A pre request -> B pre request -> C pre request -> REQUEST Execution -> C post request -> B post request -> A post request.

When you want to define a new interceptor please consider tha values described below and chose the Order value
according to its placement in execution chain, otherwise it will default to 0. See also the list below.

ORDER = 300 - Metrics Interceptor that has to execute last to give us correct values
ORDER = 200 - Logging Interceptor this interceptor logs request therefore it's important to place it after any interceptors that modify request e.g. the ones adding headers.
ORDER = 150 - Publishing Interceptor this interceptor publishes the RawData on Kafka 
0 < ORDER < 150 - Interceptors that needs to be executed AFTER DefaultOrderClientHttpRequestInterceptorWrapper but BEFORE PublishingInterceptor
ORDER = 0 - DefaultOrderClientHttpRequestInterceptorWrapper all declared interceptors that doesn't provide Order value
ORDER < 0 - Any interceptors that should execute before Default ones 
