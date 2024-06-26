package edu.java.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public abstract class HttpClient {
    protected final WebClient webClient;
    protected final ObjectMapper objectMapper;
    protected final Retry retryPolicy;

    public HttpClient(String baseUrl, Retry retryPolicy) {
        this(baseUrl, new HttpHeaders(), retryPolicy);
    }

    public HttpClient(String baseUrl, HttpHeaders headers, Retry retryPolicy) {
        this.webClient = org.springframework.web.reactive.function.client.WebClient
            .builder()
            .baseUrl(baseUrl)
            .defaultHeaders(httpHeaders -> httpHeaders.addAll(headers))
            .build();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper.findAndRegisterModules();
        this.retryPolicy = retryPolicy;
    }

    protected String getResponse(
        String path,
        MultiValueMap<String, String> params,
        String notFoundMessage,
        Set<HttpStatusCode> retryCodes
    ) throws IllegalArgumentException {
        return webClient
            .get()
            .uri(uriBuilder -> uriBuilder
                .path(path)
                .queryParams(params)
                .build())
            .retrieve()
            .onStatus(
                status -> status == HttpStatus.NOT_FOUND,
                clientResponse -> Mono.error(new IllegalArgumentException(notFoundMessage))
            )
            .onStatus(
                retryCodes::contains,
                clientResponse -> Mono.error(new ServiceException("service exception"))
            )
            .bodyToMono(String.class)
            .retryWhen(retryPolicy)
            .block();
    }

    protected Mono<String> postRequest(String path, Object body)
        throws IllegalArgumentException {
        return webClient
            .post()
            .uri(uriBuilder -> uriBuilder
                .path(path)
                .build())
            .bodyValue(body)
            .retrieve()
            .bodyToMono(String.class);
    }
}
