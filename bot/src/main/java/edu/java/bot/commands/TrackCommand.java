package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.DAO.UserDAO;
import edu.java.bot.model.UserModel;
import java.util.Optional;

public class TrackCommand extends CompositeCommand {
    private final String OK_MESSAGE = "Теперь ваша ссылка отслеживается";
    private final String WRONG_URL_MESSAGE = "Неверный формат ссылки, попробуйте ещё раз";

    public TrackCommand(UserDAO userDAO) {
        super(userDAO);
    }

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String argumentName() {
        return "<url>";
    }

    @Override
    public String description() {
        return "Начать отслеживать ссылку";
    }

    @Override
    public SendMessage handle(Update update) {
        String url = update.message().text().split(" ")[1];
        Long userId = update.message().from().id();
        userDAO.addTrackedLink(userId, url);
        return new SendMessage(update.message().chat().id(), OK_MESSAGE);
    }
}
