package edu.java.configuration;

import edu.java.dao.repository.chatRepository.ChatRepository;
import edu.java.dao.repository.chatRepository.JdbcChatRepository;
import edu.java.dao.repository.linkAndChatRepository.JdbcLinkAndChatRepository;
import edu.java.dao.repository.linkRepository.JdbcLinkRepository;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.annotation.Validated;
import javax.sql.DataSource;

@Validated
@ConfigurationProperties(prefix = "spring.datasource", ignoreUnknownFields = false)
@EnableTransactionManagement
public record DatabaseConfig(@NotEmpty String url, @NotEmpty String username, @NotEmpty String password,
                             @NotEmpty String driver) {

    @Bean
    public JdbcChatRepository chatRepository() {
        return new JdbcChatRepository(dataSource());
    }

    @Bean
    public JdbcLinkRepository jdbcLinkRepository() {
        return new JdbcLinkRepository(dataSource());
    }

    @Bean
    public JdbcLinkAndChatRepository jdbcLinkAndChatRepository() {
        return new JdbcLinkAndChatRepository(dataSource());
    }

    private DataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setUrl(url);
        driverManagerDataSource.setUsername(username);
        driverManagerDataSource.setPassword(password);
        driverManagerDataSource.setDriverClassName(driver);
        return driverManagerDataSource;
    }
}
