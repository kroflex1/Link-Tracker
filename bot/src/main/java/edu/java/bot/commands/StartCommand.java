package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;

public class StartCommand extends Command {
    private static final String USER_ALREADY_REGISTERED_MESSAGE = "Привет, вы уже пользуетесь данным ботом";
    private static final String WELCOME_MESSAGE = "Привет, чтобы получить список доступных команд, используй /help";

    public StartCommand(ScrapperClient scrapperClient) {
        super(scrapperClient);
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
        Long chatId = update.message().chat().id();
        try {
            scrapperClient.registerChat(chatId);
        } catch (IllegalArgumentException e) {
            return new SendMessage(chatId, USER_ALREADY_REGISTERED_MESSAGE);
        }
        return new SendMessage(
            chatId,
            WELCOME_MESSAGE
        );
    }
}
