package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.client.ScrapperClient;

public abstract class CompositeCommand extends Command {

    public CompositeCommand(ScrapperClient scrapperClient) {
        super(scrapperClient);
    }

    public abstract String argumentName();

    @Override
    public boolean supports(Update update) {
        String[] partsOfCommand = update.message().text().split(" ");
        if (partsOfCommand.length != 2) {
            return false;
        }
        String firstPartOfCommand = partsOfCommand[0];
        return update.message() != null && firstPartOfCommand.equals(command());
    }
}
