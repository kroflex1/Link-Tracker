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
        String firstPartOfCommand = update.message().text().split(" ")[0];
        return update.message() != null && firstPartOfCommand.equals(command());
    }
}
