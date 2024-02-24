package edu.java.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public abstract class HttpClient {

    protected final static String DEFAULT_URL = null;
    private final WebClient webClient;
    protected final ObjectMapper objectMapper;

    public HttpClient() {
        this(DEFAULT_URL);
    }

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
    }

    protected String getResponse(String path, MultiValueMap<String, String> params, String notFoundMessage) {
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
}
