package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.DAO.UserDAO;

public abstract class CompositeCommand extends Command {
    public CompositeCommand(UserDAO userDAO) {
        super(userDAO);
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
