package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.DAO.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

public class HelpCommand extends Command {

    public HelpCommand(UserDAO userDAO) {
        super(userDAO);
    }

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "Вывести список доступных команд";
    }

    @Override
    public SendMessage handle(Update update) {
//        StringBuilder resultText = new StringBuilder();
//        for (Command command : CommandsManager.getAllCommands()) {
//            resultText.append(String.format("%s - %s", command.command(), command.description())).append("\n");
//        }
        return new SendMessage(update.message().chat().id(), "Пока в работе");
    }
}
