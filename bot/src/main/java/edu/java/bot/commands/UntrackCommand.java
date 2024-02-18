package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.DAO.UserDAO;

public class UntrackCommand extends CompositeCommand {
    public UntrackCommand(UserDAO userDAO) {
        super(userDAO);
    }

    @Override
    public String command() {
        return "/untrack";
    }

    public String argumentName() {return "<url>";}

    @Override
    public String description() {
        return "Перестать отслеживать ссылку";
    }

    @Override
    public SendMessage handle(Update update) {

        return new SendMessage(update.message().chat().id(), "untrack");
    }
}
