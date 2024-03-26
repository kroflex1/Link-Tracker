package edu.java.configuration;

import edu.java.dao.repository.jdbc.JdbcChatRepository;
import edu.java.dao.repository.jdbc.JdbcLinkAndChatRepository;
import edu.java.dao.repository.jdbc.JdbcLinkRepository;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {
    @Autowired
    DataSource dataSource;

    @Bean
    public JdbcChatRepository jdbcChatRepository() {
        return new JdbcChatRepository(dataSource);
    }

    @Bean
    public JdbcLinkRepository jdbcLinkRepository() {
        return new JdbcLinkRepository(dataSource);
    }

    @Bean
    public JdbcLinkAndChatRepository jdbcLinkAndChatRepository() {
        return new JdbcLinkAndChatRepository(dataSource);
    }
}
