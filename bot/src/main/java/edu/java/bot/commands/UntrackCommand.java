package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class UntrackCommand implements Command {
    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "перестать отслеживать ссылку";
    }

    @Override
    public SendMessage handle(Update update) {
        return null;
    }
}
