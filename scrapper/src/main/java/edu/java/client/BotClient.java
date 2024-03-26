package edu.java.client;

import edu.java.request.LinkUpdateRequest;
import java.net.URI;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class BotClient extends HttpClient {

    public BotClient(@NotNull String baseUrl) {
        super(baseUrl);
    }

    public String sendUpdate(List<Long> chatsId, URI updatedLink, String description) {
        LinkUpdateRequest body = new LinkUpdateRequest(updatedLink.toString(), description, chatsId);
        return postRequest("/updates", body).block();
    }
}
