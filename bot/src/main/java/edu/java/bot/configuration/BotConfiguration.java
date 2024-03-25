package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.commands.Command;
import edu.java.bot.messageProcessors.UserMessageProcessor;
import java.util.List;
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
    public UserMessageProcessor userMessageProcessor(@Autowired List<Command> commands) {
        return new UserMessageProcessor(commands);
    }
}
