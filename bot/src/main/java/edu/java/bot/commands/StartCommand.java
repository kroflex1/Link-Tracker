package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dao.ChatDAO;

public class StartCommand extends Command {
    private static final String USER_ALREADY_REGISTERED_MESSAGE = "Привет, вы уже пользуетесь данным ботом";

    public StartCommand(ChatDAO chatDAO) {
        super(chatDAO);
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
        chatDAO.addChat(chatId);
        return new SendMessage(
            chatId,
            "Привет, чтобы получить список доступных команд, используй /help"
        );
    }
}
