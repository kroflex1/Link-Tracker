package edu.java;

import edu.java.configuration.ApplicationConfig;
import edu.java.configuration.DatabaseConfig;
import edu.java.configuration.TransactionConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({DatabaseConfig.class, TransactionConfig.class})
@EnableConfigurationProperties({ApplicationConfig.class})
public class ScrapperApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScrapperApplication.class, args);
    }
}
