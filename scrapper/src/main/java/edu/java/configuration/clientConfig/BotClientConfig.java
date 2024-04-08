package edu.java.configuration.clientConfig;

import edu.java.client.BotClient;
import edu.java.client.retry.RetryPolicy;
import io.micrometer.common.lang.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotClientConfig {
    @Value("${bot.url}")
    private String botLink;

    @Nullable
    @Value("${bot.retry-policy}")
    RetryPolicy retryPolicy;

    @Nullable
    @Value("${bot.max-attempts}")
    int maxAttempts;

    @Nullable
    @Value("${bot.time}")
    long time;

    @Bean
    public BotClient botClient() {
        return new BotClient(botLink);
    }
}
