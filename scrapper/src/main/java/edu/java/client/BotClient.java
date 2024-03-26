package edu.java.client;

import java.net.URI;
import java.util.List;
import edu.java.request.LinkUpdateRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class BotClient extends HttpClient {
    ;

    public BotClient(@NotNull String baseUrl) {
        super(baseUrl);
    }

    public String sendUpdate(List<Long> chatsId, URI updatedLink, String description) {
        LinkUpdateRequest body = new LinkUpdateRequest(updatedLink.toString(), description, chatsId);
        return postRequest("/updates", body).block();
    }
}
