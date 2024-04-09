package edu.java.client;

import edu.java.retryPolicy.RetryPolicy;
import edu.java.request.LinkUpdateRequest;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import org.hibernate.service.spi.ServiceException;
import org.jetbrains.annotations.NotNull;
import reactor.util.retry.Retry;

public class BotClient extends HttpClient {

    private static final Retry DEFAULT_RETRY_POLICY =
        RetryPolicy.CONSTANT.createWith(2, Duration.ofSeconds(2));

    public BotClient(@NotNull String baseUrl) {
        super(baseUrl, DEFAULT_RETRY_POLICY);
    }

    public String sendUpdate(List<Long> chatsId, URI updatedLink, String description) {
        LinkUpdateRequest body = new LinkUpdateRequest(updatedLink.toString(), description, chatsId);
        return postRequest("/updates", body).block();
    }
}
