package edu.java.configuration.clientConfig;

import edu.java.client.StackOverflowClient;
import edu.java.client.retry.RetryPolicy;
import io.micrometer.common.lang.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import java.time.Duration;

@Configuration
public class StackOverflowClientConfig {
    @Value("${stackoverflow.url}")
    private String stackOverflowUrl;

    @Nullable
    @Value("${stackoverflow.retry-policy}")
    RetryPolicy retryPolicy;

    @Nullable
    @Value("${stackoverflow.max-attempts}")
    int maxAttempts;

    @Nullable
    @Value("${stackoverflow.time}")
    long time;

    @Bean
    public StackOverflowClient stackOverflowClient() {
        if (retryPolicy == null) {
            return new StackOverflowClient(stackOverflowUrl, new HttpHeaders());
        }
        return new StackOverflowClient(stackOverflowUrl, new HttpHeaders(), retryPolicy.createWith(
            maxAttempts,
            Duration.ofMillis(time)
        ));
    }
}
