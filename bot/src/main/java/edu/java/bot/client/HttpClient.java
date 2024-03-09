package edu.java.bot.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

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
}
