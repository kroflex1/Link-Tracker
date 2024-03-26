package edu.java.bot.client;

import edu.java.request.AddLinkRequest;
import edu.java.response.LinkResponse;
import edu.java.response.ListLinksResponse;
import edu.java.response.RemoveLinkResponse;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

@Validated
public class ScrapperClient extends HttpClient {
    public static final String NOT_FOUND_CHAT_ID_MESSAGE = "Chat with this id isn`t registered";
    public static final String CHAT_ALREADY_REGISTERED_MESSAGE = "Chat with this id already registered";
    public static final String ALREADY_TRACKED_LINK_MESSAGE = "Chat already tracking this link";

    public ScrapperClient(@NotNull String baseUrl) {
        super(baseUrl);
    }

    @SuppressWarnings("MultipleStringLiterals")
    public void registerChat(Long chatId) throws IllegalArgumentException {
        webClient
            .post()
            .uri(uriBuilder -> uriBuilder
                .path("/tg-chat/%d".formatted(chatId))
                .build())
            .retrieve()
            .onStatus(
                status -> status == HttpStatus.CONFLICT,
                clientResponse -> Mono.error(new IllegalArgumentException(CHAT_ALREADY_REGISTERED_MESSAGE))
            )
            .bodyToMono(String.class)
            .block();
    }

    @SuppressWarnings("MultipleStringLiterals")
    public void removeChat(Long chatId) throws IllegalArgumentException {
        webClient
            .delete()
            .uri(uriBuilder -> uriBuilder
                .path("/tg-chat/%d".formatted(chatId))
                .build())
            .retrieve()
            .onStatus(
                status -> status == HttpStatus.BAD_REQUEST,
                clientResponse -> Mono.error(new IllegalArgumentException(NOT_FOUND_CHAT_ID_MESSAGE))
            )
            .bodyToMono(String.class)
            .block();
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
    public void trackLink(Long chatId, URI link) throws IllegalArgumentException {
        AddLinkRequest addLinkBody = new AddLinkRequest(link.toString());
        webClient
            .post()
            .uri(uriBuilder -> uriBuilder
                .path("/links/%d".formatted(chatId))
                .build())
            .bodyValue(addLinkBody)
            .retrieve()
            .onStatus(
                status -> status == HttpStatus.BAD_REQUEST,
                clientResponse -> Mono.error(new IllegalArgumentException(NOT_FOUND_CHAT_ID_MESSAGE))
            )
            .onStatus(
                status -> status == HttpStatus.CONFLICT,
                clientResponse -> Mono.error(new IllegalArgumentException(ALREADY_TRACKED_LINK_MESSAGE))
            )
            .bodyToMono(String.class)
            .block();
    }

    @SuppressWarnings("MultipleStringLiterals")
    public void stopTrackLink(Long chatId, URI link) throws IllegalArgumentException {
        RemoveLinkResponse body = new RemoveLinkResponse(link);
        webClient
            .method(HttpMethod.DELETE)
            .uri(uriBuilder -> uriBuilder
                .path("/links/%d".formatted(chatId))
                .build())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .retrieve()
            .onStatus(
                status -> status == HttpStatus.BAD_REQUEST,
                clientResponse -> Mono.error(new IllegalArgumentException(NOT_FOUND_CHAT_ID_MESSAGE))
            )
            .bodyToMono(String.class)
            .block();
    }
}
