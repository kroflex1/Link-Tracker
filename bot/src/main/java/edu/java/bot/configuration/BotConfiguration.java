package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UntrackCommand;
import edu.java.bot.messageProcessors.CommandMessageProcessor;
import edu.java.bot.messageProcessors.UserMessageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class BotConfiguration {

    @Bean
    public TelegramBot telegramBot(@Autowired ApplicationConfig applicationConfig) {
        return new TelegramBot(applicationConfig.telegramToken());
    }

    @Bean
    public UserMessageProcessor userMessageProcessor() {
        return new CommandMessageProcessor(List.of(
            new HelpCommand(),
            new ListCommand(),
            new StartCommand(),
            new TrackCommand(),
            new UntrackCommand()
        ));
    }
}
