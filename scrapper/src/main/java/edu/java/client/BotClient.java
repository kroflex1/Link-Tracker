package edu.java.client;

import java.net.URI;
import java.util.List;
import edu.java.request.LinkUpdateRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

@Validated
public class BotClient extends HttpClient {
    public BotClient(@NotNull String baseUrl) {
        super(baseUrl);
    }

    public String sendUpdate(List<Long> chatsId, URI updatedLink, String description) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("tgChatsId", chatsId);
        builder.part("url", updatedLink.toString());
        builder.part("description", description);
        MultiValueMap<String, HttpEntity<?>> body = builder.build();
        return postRequest("/updates", body).block();
    }
}
