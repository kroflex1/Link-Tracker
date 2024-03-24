package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import java.net.URI;
import java.util.List;

public class ListCommand extends Command {
    private static final String FILLED_LIST_MESSAGE = "Список отслеживаемых ссылок:";
    private static final String EMPTY_LIST_LINKS_MESSAGE = "У вас нет отслеживаемых ссылок";

    public ListCommand(ScrapperClient scrapperClient) {
        super(scrapperClient);
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
        List<URI> trackedLinks;
        try {
            trackedLinks = scrapperClient.getTrackedLinks(chatID);
        } catch (IllegalArgumentException e) {
            return new SendMessage(update.message().chat().id(), EMPTY_LIST_LINKS_MESSAGE);
        }
        if (trackedLinks.isEmpty()) {
            return new SendMessage(update.message().chat().id(), EMPTY_LIST_LINKS_MESSAGE);
        }
        StringBuilder text = new StringBuilder(FILLED_LIST_MESSAGE).append("\n");
        for (int i = 0; i < trackedLinks.size(); i++) {
            text.append(i + 1).append(") ").append(trackedLinks.get(i).toString()).append("\n");
        }
        return new SendMessage(update.message().chat().id(), text.toString());
    }
}
