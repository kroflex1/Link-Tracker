package edu.java.bot.client;

import com.fasterxml.jackson.databind.JsonNode;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

@Validated
public class ScrapperClient extends HttpClient {

    private static final String NOT_FOUND_CHAT_ID_MESSAGE = "Chat with this id isn`t registered";

    public ScrapperClient(@NotNull String baseUrl) {
        super(baseUrl);
    }

    public Mono<String> registerChat(Long chatId) {
        return webClient
            .post()
            .uri(uriBuilder -> uriBuilder
                .path("/tg-chat/%d".formatted(chatId))
                .build())
            .retrieve()
            .bodyToMono(String.class);
    }

    public Mono<String> removeChat(Long chatId) {
        return webClient
            .delete()
            .uri(uriBuilder -> uriBuilder
                .path("/tg-chat/%d".formatted(chatId))
                .build())
            .retrieve()
            .bodyToMono(String.class);
    }

    public List<URI> getTrackedLinks(Long chatId) {
        JsonNode node = webClient
            .delete()
            .uri(uriBuilder -> uriBuilder
                .path("/links/%d".formatted(chatId))
                .build())
            .retrieve()
            .onStatus(
                status -> status == HttpStatus.BAD_REQUEST,
                clientResponse -> Mono.error(new IllegalArgumentException(NOT_FOUND_CHAT_ID_MESSAGE))
            )
            .bodyToMono(JsonNode.class)
            .block();

        List<URI> result = new ArrayList<>();
        for (JsonNode link : node.get("links")) {
            result.add(URI.create(link.get("url").asText()));
        }
        return result;
    }

    public Mono<String> trackLink(Long chatId, URI link) {
        MultipartBodyBuilder body = new MultipartBodyBuilder();
        body.part("link", link.toString());
        return webClient
            .post()
            .uri(uriBuilder -> uriBuilder
                .path("/links/%d".formatted(chatId))
                .build())
            .bodyValue(body.build())
            .retrieve()
            .onStatus(
                status -> status == HttpStatus.BAD_REQUEST,
                clientResponse -> Mono.error(new IllegalArgumentException(NOT_FOUND_CHAT_ID_MESSAGE))
            )
            .bodyToMono(String.class);
    }


    public Mono<String> stopTrackLink(Long chatId, URI link) {
        MultipartBodyBuilder body = new MultipartBodyBuilder();
        body.part("link", link.toString());
        return webClient
            .method(HttpMethod.DELETE)
            .uri(uriBuilder -> uriBuilder
                .path("/links/%d".formatted(chatId))
                .build())
            .bodyValue(body.build())
            .retrieve()
            .onStatus(
                status -> status == HttpStatus.BAD_REQUEST,
                clientResponse -> Mono.error(new IllegalArgumentException(NOT_FOUND_CHAT_ID_MESSAGE))
            )
            .bodyToMono(String.class);
    }
}
