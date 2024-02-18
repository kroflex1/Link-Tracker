package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.DAO.UserDAO;
import java.util.Set;

public class ListCommand extends Command {
    private final String FILLED_LIST_MESSAGE = "Список отслеживаемых ссылок:";
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
        Long userID = update.message().from().id();
        Set<String> links = userDAO.getUserById(userID).get().getLinks();
        if (links.isEmpty()) {
            return new SendMessage(update.message().chat().id(), EMPTY_LIST_LINKS_MESSAGE);
        }
        StringBuilder text = new StringBuilder(FILLED_LIST_MESSAGE).append("\n");
        for (String url : userDAO.getUserById(userID).get().getLinks()) {
            text.append(url).append("\n");
        }
        return new SendMessage(update.message().chat().id(), text.toString());
    }
}
