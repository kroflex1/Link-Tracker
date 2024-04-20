package edu.java.configuration.daoConfig;

import edu.java.dao.repository.jpa.JpaChatRepository;
import edu.java.dao.repository.jpa.JpaLinkRepository;
import edu.java.dao.service.ChatService;
import edu.java.dao.service.LinkService;
import edu.java.dao.service.jpa.JpaChatService;
import edu.java.dao.service.jpa.JpaLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfiguration {

    private final JpaChatRepository jpaChatRepository;
    private final JpaLinkRepository jpaLinkRepository;

    @Autowired
    public JpaAccessConfiguration(JpaChatRepository jpaChatRepository, JpaLinkRepository jpaLinkRepository) {
        this.jpaChatRepository = jpaChatRepository;
        this.jpaLinkRepository = jpaLinkRepository;
    }

    @Bean
    public LinkService linkService() {
        return new JpaLinkService(jpaLinkRepository, jpaChatRepository);
    }

    @Bean
    public ChatService chatService() {
        return new JpaChatService(jpaChatRepository, jpaLinkRepository);
    }


}
