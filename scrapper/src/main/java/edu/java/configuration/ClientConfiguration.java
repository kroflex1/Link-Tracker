package edu.java.configuration;

import edu.java.client.BotClient;
import edu.java.client.GitHubClient;
import edu.java.client.StackOverflowClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class ClientConfiguration {
    @Value("${github.token}")
    private String githubToken;
    @Value("${github.url}")
    private String githubUrl;
    @Value("&{stackoverflow.url}")
    private String stackOverflowUrl;
    @Value("${bot.url}")
    private String botLink;

    @Bean
    public GitHubClient gitHubClient() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer" + githubToken);
        headers.add("User-Agent", "LinkBot");
        headers.add("Accept", "application/json");
        return new GitHubClient(githubUrl, headers);
    }

    @Bean
    public StackOverflowClient stackOverflowClient() {
        return new StackOverflowClient(stackOverflowUrl, new HttpHeaders());
    }

    @Bean
    public BotClient botClient() {
        return new BotClient(botLink);
    }
}
