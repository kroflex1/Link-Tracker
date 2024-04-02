package edu.java.configuration.daoConfig;

import edu.java.dao.repository.jdbc.JdbcChatRepository;
import edu.java.dao.repository.jdbc.JdbcLinkAndChatRepository;
import edu.java.dao.repository.jdbc.JdbcLinkRepository;
import edu.java.dao.service.ChatService;
import edu.java.dao.service.LinkService;
import edu.java.dao.service.jdbc.JdbcChatService;
import edu.java.dao.service.jdbc.JdbcLinkService;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfiguration {
    final DataSource dataSource;
    final JdbcChatRepository jdbcChatRepository;
    final JdbcLinkRepository jdbcLinkRepository;
    final JdbcLinkAndChatRepository jdbcLinkAndChatRepository;

    @Autowired
    public JdbcAccessConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcChatRepository = new JdbcChatRepository(dataSource);
        this.jdbcLinkRepository = new JdbcLinkRepository(dataSource);
        this.jdbcLinkAndChatRepository = new JdbcLinkAndChatRepository(dataSource);
    }

    @Bean
    public ChatService jdbcChatService() {
        return new JdbcChatService(jdbcChatRepository, jdbcLinkAndChatRepository);
    }

    @Bean
    LinkService jdbcLinkService() {
        return new JdbcLinkService(jdbcLinkRepository, jdbcLinkAndChatRepository);
    }

    @Bean
    public JdbcChatRepository jdbcChatRepository() {
        return jdbcChatRepository;
    }

    @Bean
    public JdbcLinkRepository jdbcLinkRepository() {
        return jdbcLinkRepository;
    }

    @Bean
    public JdbcLinkAndChatRepository jdbcLinkAndChatRepository() {
        return jdbcLinkAndChatRepository;
    }

}
