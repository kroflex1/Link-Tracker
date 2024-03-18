package edu.java.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public abstract class HttpClient {

    protected final WebClient webClient;
    protected final ObjectMapper objectMapper;

    public HttpClient(String baseUrl) {
        this(baseUrl, new HttpHeaders());
    }

    public HttpClient(String baseUrl, HttpHeaders headers) {
        this.webClient = WebClient
            .builder()
            .baseUrl(baseUrl)
            .defaultHeaders(httpHeaders -> httpHeaders.addAll(headers))
            .build();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper.findAndRegisterModules();
    }

    protected String getResponse(String path, MultiValueMap<String, String> params, String notFoundMessage)
        throws IllegalArgumentException {
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
            .bodyToMono(String.class)
            .block();
    }

    protected String getResponse(String path, String notFoundMessage) {
        return getResponse(path, new LinkedMultiValueMap<>(), notFoundMessage);
    }

    protected Mono<String> postRequest(String path, MultiValueMap<String, HttpEntity<?>> body)
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
