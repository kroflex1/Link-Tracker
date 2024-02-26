package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.DAO.UserDAO;
import edu.java.bot.model.UserModel;
import java.util.Optional;

public class UntrackCommand extends CompositeCommand {
    private static final String OK_MESSAGE = "Ссылка больше не отслеживается";
    private static final String REQUIRED_LINK_IS_MISSING_MESSAGE = "Переданная ссылка не найдена";

    public UntrackCommand(UserDAO userDAO) {
        super(userDAO);
    }

    @Override
    public String command() {
        return "/untrack";
    }

    public String argumentName() {
        return "<url>";
    }

    @Override
    public String description() {
        return "Перестать отслеживать ссылку";
    }

    @Override
    public SendMessage handle(Update update) {
        String url = update.message().text().split(" ")[1];
        Long userId = update.message().from().id();
        Optional<UserModel> userModel = userDAO.getUserById(userId);
        if (userModel.isEmpty() || !userModel.get().getLinks().contains(url)) {
            return new SendMessage(update.message().chat().id(), REQUIRED_LINK_IS_MISSING_MESSAGE);
        }
        userDAO.removeTrackedLink(userId, url);
        return new SendMessage(update.message().chat().id(), OK_MESSAGE);
    }
}
