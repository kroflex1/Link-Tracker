package edu.java.configuration;

import edu.java.client.BotClient;
import edu.java.client.StackOverflowClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class ClientConfiguration {

    @Value("${stackoverflow.url}")
    private String stackOverflowUrl;
    @Value("${bot.url}")
    private String botLink;



    @Bean
    public StackOverflowClient stackOverflowClient() {
        return new StackOverflowClient(stackOverflowUrl, new HttpHeaders());
    }

    @Bean
    public BotClient botClient() {
        return new BotClient(botLink);
    }
}
