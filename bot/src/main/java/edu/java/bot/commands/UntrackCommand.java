package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import java.net.URI;

public class UntrackCommand extends CompositeCommand {
    private static final String OK_MESSAGE = "Ссылка больше не отслеживается";
    private static final String REQUIRED_LINK_IS_MISSING_MESSAGE = "Переданная ссылка не найдена";

    public UntrackCommand(ScrapperClient scrapperClient) {
        super(scrapperClient);
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
        String link = update.message().text().split(" ")[1];
        Long chatId = update.message().chat().id();
        try {
            scrapperClient.stopTrackLink(chatId, URI.create(link));
        } catch (IllegalArgumentException e) {
            return new SendMessage(update.message().chat().id(), REQUIRED_LINK_IS_MISSING_MESSAGE);
        }
        return new SendMessage(update.message().chat().id(), OK_MESSAGE);
    }
}
