package edu.java.configuration;

import edu.java.client.GitHubClient;
import edu.java.client.StackOverflowClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {
    @Value("${github.token}")
    private String githubToken;
    @Value("${github.url}")
    private String githubUrl;

    @Bean
    public GitHubClient gitHubClient() {
        return new GitHubClient(githubUrl, githubToken);
    }

    @Bean StackOverflowClient stackOverflowClient() {
        return new StackOverflowClient();
    }

}
