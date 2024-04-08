package edu.java.configuration.clientConfig;

import edu.java.client.GitHubClient;
import edu.java.client.retry.RetryPolicy;
import io.micrometer.common.lang.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import java.time.Duration;

@Configuration
public class GitHubClientConfig {
    @Value("${github.token}")
    private String githubToken;

    @Value("${github.url}")
    private String githubUrl;

    @Nullable
    @Value("${github.retry-policy}")
    private RetryPolicy retryPolicy;

    @Nullable
    @Value("${github.max-attempts}")
    private int maxAttempts;

    @Nullable
    @Value("${github.time}")
    private long time;

    @Bean
    public GitHubClient gitHubClient() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer %s".formatted(githubToken));
        headers.add("User-Agent", "LinkBot");
        headers.add("Accept", "application/json");
        if (retryPolicy == null) {
            return new GitHubClient(githubUrl, headers);
        }
        return new GitHubClient(githubUrl, headers, retryPolicy.createWith(maxAttempts, Duration.ofMillis(time)));
    }
}
