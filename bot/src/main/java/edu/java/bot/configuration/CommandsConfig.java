package edu.java.bot.configuration;

import edu.java.bot.client.ScrapperClient;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UntrackCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class CommandsConfig {
    @Bean
    public List<Command> commands(@Autowired ScrapperClient scrapperClient) {
        return List.of(
            new HelpCommand(scrapperClient),
            new StartCommand(scrapperClient),
            new ListCommand(scrapperClient),
            new TrackCommand(scrapperClient),
            new UntrackCommand(scrapperClient)
        );
    }
}
