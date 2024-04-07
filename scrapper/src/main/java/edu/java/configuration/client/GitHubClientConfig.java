package edu.java.configuration.client;

import edu.java.client.GitHubClient;
import io.micrometer.common.lang.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import reactor.util.retry.Retry;
import java.time.Duration;

@Configuration
public class GitHubClientConfig() {
    @Value("${github.token}")
    private String githubToken;

    @Value("${github.url}")
    private String githubUrl;

    @Nullable
    @Value("${github.retry-policy}")
    RetryPolicy retryPolicy;

    @Nullable
    @Value("${github.max-attempts}")
    int maxAttempts;

    @Nullable
    @Value("${github.time}")
    long time;

    @Bean
    public GitHubClient gitHubClient() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer %s".formatted(githubToken));
        headers.add("User-Agent", "LinkBot");
        headers.add("Accept", "application/json");
        Retry retry = retryPolicy.getRetry(maxAttempts, Duration.ofMillis(time));
        return new GitHubClient(githubUrl, headers, retry);
    }
}
