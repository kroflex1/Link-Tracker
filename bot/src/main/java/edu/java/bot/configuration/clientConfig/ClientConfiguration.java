package edu.java.bot.configuration.clientConfig;

import edu.java.bot.client.ScrapperClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {
    @Value("${scrapper.url}")
    private String scrapperUrl;

    @Bean
    public ScrapperClient scrapperClient() {
        return new ScrapperClient(scrapperUrl);
    }
}
