package edu.java.client;

import java.net.URI;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;

@Validated
public class BotClient extends HttpClient {
    public BotClient(@NotNull String baseUrl) {
        super(baseUrl);
    }

    public void sendUpdate(List<Long> chatsId, URI updatedLink, String description) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("chatsId", chatsId);
        builder.part("url", updatedLink.toString());
        builder.part("description", description);
        MultiValueMap<String, HttpEntity<?>> body = builder.build();
        postRequest("/updates", body);
    }

}
