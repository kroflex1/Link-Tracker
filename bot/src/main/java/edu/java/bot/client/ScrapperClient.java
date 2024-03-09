package edu.java.bot.client;

import edu.java.response.LinkResponse;
import edu.java.response.ListLinksResponse;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

@Validated
public class ScrapperClient extends HttpClient {

    private static final String NOT_FOUND_CHAT_ID_MESSAGE = "Chat with this id isn`t registered";

    public ScrapperClient(@NotNull String baseUrl) {
        super(baseUrl);
    }

    @SuppressWarnings("MultipleStringLiterals")
    public Mono<String> registerChat(Long chatId) {
        return webClient
            .post()
            .uri(uriBuilder -> uriBuilder
                .path("/tg-chat/%d".formatted(chatId))
                .build())
            .retrieve()
            .bodyToMono(String.class);
    }

    @SuppressWarnings("MultipleStringLiterals")
    public Mono<String> removeChat(Long chatId) {
        return webClient
            .delete()
            .uri(uriBuilder -> uriBuilder
                .path("/tg-chat/%d".formatted(chatId))
                .build())
            .retrieve()
            .bodyToMono(String.class);
    }

    @SuppressWarnings("MultipleStringLiterals")
    public List<URI> getTrackedLinks(Long chatId) throws IllegalArgumentException {
        ListLinksResponse linksInf = webClient
            .get()
            .uri(uriBuilder -> uriBuilder
                .path("/links/%d".formatted(chatId))
                .build())
            .retrieve()
            .onStatus(
                status -> status == HttpStatus.BAD_REQUEST,
                clientResponse -> Mono.error(new IllegalArgumentException(NOT_FOUND_CHAT_ID_MESSAGE))
            )
            .bodyToMono(ListLinksResponse.class)
            .block();

        List<URI> result = new ArrayList<>();
        for (LinkResponse linkInf : linksInf.links()) {
            result.add(linkInf.link());
        }
        return result;
    }

    @SuppressWarnings("MultipleStringLiterals")
    public Mono<String> trackLink(Long chatId, URI link) {
        MultipartBodyBuilder body = new MultipartBodyBuilder();
        body.part("link", link.toString());
        return webClient
            .post()
            .uri(uriBuilder -> uriBuilder
                .path("/links/%d".formatted(chatId))
                .build())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(body.build())
            .retrieve()
            .onStatus(
                status -> status == HttpStatus.BAD_REQUEST,
                clientResponse -> Mono.error(new IllegalArgumentException(NOT_FOUND_CHAT_ID_MESSAGE))
            )
            .bodyToMono(String.class);
    }

    @SuppressWarnings("MultipleStringLiterals")
    public Mono<String> stopTrackLink(Long chatId, URI link) {
        MultipartBodyBuilder body = new MultipartBodyBuilder();
        body.part("link", link.toString());
        return webClient
            .method(HttpMethod.DELETE)
            .uri(uriBuilder -> uriBuilder
                .path("/links/%d".formatted(chatId))
                .build())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(body.build())
            .retrieve()
            .onStatus(
                status -> status == HttpStatus.BAD_REQUEST,
                clientResponse -> Mono.error(new IllegalArgumentException(NOT_FOUND_CHAT_ID_MESSAGE))
            )
            .bodyToMono(String.class);
    }
}
