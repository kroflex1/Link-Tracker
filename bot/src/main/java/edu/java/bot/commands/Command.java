package edu.java.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dao.ChatDAO;

public abstract class Command {
    protected final ChatDAO chatDAO;

    public Command(ChatDAO chatDAO) {
        this.chatDAO = chatDAO;
    }

    public abstract String command();

    public abstract String description();

    public abstract SendMessage handle(Update update);

    public boolean supports(Update update) {
        return update.message() != null && update.message().text().equals(command());
    }

    public BotCommand toApiCommand() {
        return new BotCommand(command(), description());
    }
}
