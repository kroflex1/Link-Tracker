package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.DAO.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;

public class ListCommand extends Command {
    private final String EMPTY_LIST_LINKS_MESSAGE = "У вас нет отслеживаемых ссылок";

    public ListCommand(UserDAO userDAO) {
        super(userDAO);
    }

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "Показать список отслеживаемых ссылок";
    }

    @Override
    public SendMessage handle(Update update) {
        StringBuilder text = new StringBuilder();
        Long userID = update.message().from().id();
        if (!isUserRegistered(userID)) {
            registerUser(userID);
        }
        for (String url : userDAO.getUserById(userID).get().getLinks()) {
            text.append(url).append("\n");
        }
        if (text.isEmpty()) {
            return new SendMessage(update.message().chat().id(), EMPTY_LIST_LINKS_MESSAGE);
        }
        return new SendMessage(update.message().chat().id(), text.toString());
    }
}
