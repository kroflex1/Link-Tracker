package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dao.ChatDAO;
import edu.java.bot.model.ChatModel;
import java.util.Optional;

public class UntrackCommand extends CompositeCommand {
    private static final String OK_MESSAGE = "Ссылка больше не отслеживается";
    private static final String REQUIRED_LINK_IS_MISSING_MESSAGE = "Переданная ссылка не найдена";

    public UntrackCommand(ChatDAO chatDAO) {
        super(chatDAO);
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
        Long chatId = update.message().chat().id();
        Optional<ChatModel> chatModel = chatDAO.getChatById(chatId);
        if (chatModel.isEmpty() || !chatModel.get().getLinks().contains(url)) {
            return new SendMessage(update.message().chat().id(), REQUIRED_LINK_IS_MISSING_MESSAGE);
        }
        chatDAO.removeTrackedLink(chatId, url);
        return new SendMessage(update.message().chat().id(), OK_MESSAGE);
    }
}
