package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dao.ChatDAO;
import edu.java.bot.model.ChatModel;
import java.util.Optional;
import java.util.Set;

public class ListCommand extends Command {
    private static final String FILLED_LIST_MESSAGE = "Список отслеживаемых ссылок:";
    private static final String EMPTY_LIST_LINKS_MESSAGE = "У вас нет отслеживаемых ссылок";

    public ListCommand(ChatDAO chatDAO) {
        super(chatDAO);
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
        Long chatID = update.message().chat().id();
        Optional<ChatModel> chat = chatDAO.getChatById(chatID);
        if (chat.isEmpty() || chat.get().getLinks().isEmpty()) {
            return new SendMessage(update.message().chat().id(), EMPTY_LIST_LINKS_MESSAGE);
        }
        Set<String> links = chat.get().getLinks();
        StringBuilder text = new StringBuilder(FILLED_LIST_MESSAGE).append("\n");
        for (String url : links) {
            text.append(url).append("\n");
        }
        return new SendMessage(update.message().chat().id(), text.toString());
    }
}
