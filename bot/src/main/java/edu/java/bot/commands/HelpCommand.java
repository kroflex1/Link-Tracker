package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class HelpCommand implements Command{

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "Выводит список доступных команд";
    }

    @Override
    public SendMessage handle(Update update) {
        return null;
    }
}
