package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.DAO.UserDAO;

public class StartCommand extends Command {
    private final static String USER_ALREADY_REGISTERED_MESSAGE = "Привет, вы уже пользуетесь данным ботом";

    public StartCommand(UserDAO userDAO) {
        super(userDAO);
    }

    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "Начать пользоаться ботом";
    }

    @Override
    public SendMessage handle(Update update) {
        if (isUserRegistered(update.message().from().id())) {
            return new SendMessage(update.message().chat().id(), USER_ALREADY_REGISTERED_MESSAGE);
        }
        registerUser(update.message().chat().id());
        return new SendMessage(
            update.message().chat().id(),
            "Привет, чтобы получить список доступных команд, используй /help"
        );
    }
}
