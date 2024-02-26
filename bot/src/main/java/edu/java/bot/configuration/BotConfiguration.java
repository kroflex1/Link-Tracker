package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.DAO.InMemoryUserDAO;
import edu.java.bot.DAO.UserDAO;
import edu.java.bot.messageProcessors.CommandMessageProcessor;
import edu.java.bot.messageProcessors.UserMessageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfiguration {

    @Bean
    public TelegramBot telegramBot(@Autowired ApplicationConfig applicationConfig) {
        return new TelegramBot(applicationConfig.telegramToken());
    }

    @Bean
    public UserDAO userDAO() {
        return new InMemoryUserDAO();
    }

    @Bean
    public UserMessageProcessor userMessageProcessor() {
        return new CommandMessageProcessor(userDAO());
    }
}
